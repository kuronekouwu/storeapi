package dev.mottolab.storeapi.controller;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.google.gson.Gson;
import dev.mottolab.storeapi.dto.request.payment.GeneratePaymentDTO;
import dev.mottolab.storeapi.dto.request.payment.ProceedTmnVoucherDTO;
import dev.mottolab.storeapi.dto.request.payment.SlipMethodDTO;
import dev.mottolab.storeapi.dto.response.payment.PaymentCompletedDTO;
import dev.mottolab.storeapi.dto.response.payment.PaymentPromtpayResultDTO;
import dev.mottolab.storeapi.dto.response.payment.PaymentURLResultDTO;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import dev.mottolab.storeapi.exception.*;
import dev.mottolab.storeapi.provider.chillpay.exception.ChillpayCreatePaymentFail;
import dev.mottolab.storeapi.provider.chillpay.response.PaymentCreateURLResult;
import dev.mottolab.storeapi.provider.rdcw.qrcr.exception.QRCRError;
import dev.mottolab.storeapi.provider.rdcw.slipverify.exception.SlipVerifyError;
import dev.mottolab.storeapi.provider.rdcw.slipverify.response.SlipVerifyResponse;
import dev.mottolab.storeapi.provider.scbopenapi.response.PromptpayCreateResult;
import dev.mottolab.storeapi.provider.truemoney.voucher.excpetion.TmnVoucherError;
import dev.mottolab.storeapi.provider.truemoney.voucher.response.TmnVoucherResponse;
import dev.mottolab.storeapi.service.OrderService;
import dev.mottolab.storeapi.service.PaymentService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    // Parser
    private Gson gson;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.gson = new Gson();
    }

    @PostMapping("/slip/promptpay/createQrCode")
    public PaymentPromtpayResultDTO generateSlipQrCode(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody GeneratePaymentDTO payload
    ) {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                UUID.fromString(payload.orderId())
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }
        if(order.getPayment() != null){
            throw new PaymentMismatch();
        }

        return new PaymentPromtpayResultDTO(this.paymentService.generatePromptpayQrCodeWithMSISDN(order.getTotal()));
    }

    @GetMapping("/slip/banking/getInformation")
    public void getBankInformation(){
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented");
    }

    @PostMapping("/slip/doProceed")
    public PaymentCompletedDTO doProceedByVerifiySlip(
            @AuthenticationPrincipal UserInfoDetail user,
            @RequestParam("image") MultipartFile file,
            @RequestParam("order_id") UUID orderId,
            @RequestParam("method") SlipMethodDTO method

    ) throws IOException, QRCRError, QRCodeNotExist, SlipVerifyError, ParseException {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                orderId
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }

        // Check if file is image
        if(!file.getContentType().startsWith("image")){
            throw new FileRequireImage();
        }

        SlipVerifyResponse data;
        if(method == SlipMethodDTO.BANK){
            data = this.paymentService.doProceedViaSlipVerifyByBankAccount(file.getBytes(), order);
        }else if(method == SlipMethodDTO.PROMPTPAY){
            data = this.paymentService.doProceedViaSlipVerifyByPromptpay(file.getBytes(), order);
        }else{
            throw new PaymentProceedFail("Invalid payment method.");
        }

        // Parse date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

        // Create entity
        PaymentEntity entity = new PaymentEntity();
        entity.setPaidAt(sdf.parse(data.getData().getTransDate() + " " + data.getData().getTransTime()));
        entity.setAmount(order.getTotal());
        entity.setTransactionId(data.getDiscriminator());
        entity.setMethod(PaymentMethod.SLIP_VERIFY);
        entity.setMetadata(gson.toJson(data));
        // Set payment information
        order.setPayment(entity);
        // Save payment first
        PaymentEntity payment = this.paymentService.updatePayment(entity);
        // Update order
        order.setStatus(OrderStatus.SHIPPING);
        this.orderService.updateOrder(order);
        // Push event to update order
        this.paymentService.doCompletePayment(payment);

        return new PaymentCompletedDTO(payment);
    }

    @PostMapping("/truemoney/voucher/doProceed")
    @ResponseBody
    public PaymentCompletedDTO doProceedByTmnVoucher(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody ProceedTmnVoucherDTO payload
    ) throws TmnVoucherError {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                UUID.fromString(payload.orderId())
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }

        // Proceed this process
        TmnVoucherResponse tmn = this.paymentService.doProceedViaTruemoneyVoucher(payload.voucherUrl(), order);

        // Create entity
        PaymentEntity entity = new PaymentEntity();
        entity.setPaidAt(new Date(tmn.getData().getMyTicket().updateDate));
        entity.setAmount(order.getTotal());
        entity.setTransactionId(tmn.getData().getVoucher().voucherId);
        entity.setMethod(PaymentMethod.TRUEMONEY_ENVELOP);
        entity.setRef1(tmn.getData().getVoucher().link);
        entity.setMetadata(gson.toJson(tmn));
        // Set payment information
        order.setPayment(entity);
        // Save payment first
        PaymentEntity payment = this.paymentService.updatePayment(entity);
        // Update order
        order.setStatus(OrderStatus.SHIPPING);
        this.orderService.updateOrder(order);
        // Push event to update order
        this.paymentService.doCompletePayment(payment);

        return new PaymentCompletedDTO(payment);
    }

    @PostMapping("/promptpay/createPayment")
    public PaymentPromtpayResultDTO doCreatePaymentByPromptpay(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody GeneratePaymentDTO payload
    ) {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                UUID.fromString(payload.orderId())
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }

        // Get payment
        PaymentEntity payment = order.getPayment();

        if(payment == null){
            // Generate transaction ID
            Ulid uid = UlidCreator.getUlid();
            String transactionId = uid.toString().substring(6);

            PromptpayCreateResult ppResult = this.paymentService.generatePromtpayQRCodeBySCB(order, transactionId);

            // Get QRCode
            String ppRaw = ppResult.getData().getQrRawData();

            // Create entity
            PaymentEntity entity = new PaymentEntity();
            entity.setAmount(order.getTotal());
            entity.setTransactionId(transactionId);
            entity.setMethod(PaymentMethod.PROMPTPAY);
            entity.setQrCode(ppRaw);
            // Set payment information
            order.setPayment(entity);
            // Save payment first
            this.paymentService.updatePayment(entity);
            // Update order
            this.orderService.updateOrder(order);

            return new PaymentPromtpayResultDTO(ppRaw);
        }

        // If exist. Check if payment method is promptpay
        if(payment.getMethod() != PaymentMethod.PROMPTPAY){
            throw new PaymentMismatch();
        }

        return new PaymentPromtpayResultDTO(payment.getQrCode());
    }

    @PostMapping("/truemoney/createPayment")
    public PaymentURLResultDTO doCreatePaymentByTmn(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody GeneratePaymentDTO payload
    ) throws ChillpayCreatePaymentFail {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                UUID.fromString(payload.orderId())
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }

        // Generate transaction ID
        Ulid uid = UlidCreator.getUlid();
        String transactionId = uid.toString().substring(6);

        PaymentCreateURLResult payResult = this.paymentService.generateTruemoneyPaymentURLByChillPay(order, transactionId);

        // Create entity
        PaymentEntity entity = new PaymentEntity();
        entity.setAmount(order.getTotal());
        entity.setTransactionId(transactionId);
        entity.setMethod(PaymentMethod.TRUEMONEY_APP);
        entity.setRef1(String.valueOf(payResult.TransactionId));
        entity.setRef2(payResult.Token);
        // Set payment information
        order.setPayment(entity);
        // Save payment first
        this.paymentService.updatePayment(entity);
        // Update order
        this.orderService.updateOrder(order);

        return new PaymentURLResultDTO(payResult.getPaymentUrl());
    }
}
