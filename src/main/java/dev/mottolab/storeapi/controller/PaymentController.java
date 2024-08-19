package dev.mottolab.storeapi.controller;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.google.gson.Gson;
import com.google.zxing.NotFoundException;
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
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    // Parser
    private final Gson gson = new Gson();

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
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
        if(order.getPayment().getMethod() != PaymentMethod.SLIP_VERIFY_VIA_PROMPTPAY){
            throw new PaymentMismatch();
        }

        // Caculate total
        PaymentPromtpayResultDTO response = new PaymentPromtpayResultDTO();
        response.setAmount(order.getPayment().getAmount());
        response.setQrCode(this.paymentService.generatePromptpayQrCodeWithMSISDN(response.getAmount()));

        return response;
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

    ) throws IOException, NotFoundException, QRCodeNotExist, SlipVerifyError, ParseException {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                orderId
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }


        // Check if file is image
        if(!Objects.requireNonNull(file.getContentType()).startsWith("image")){
            throw new FileRequireImage();
        }


        SlipVerifyResponse data;
        if(method == SlipMethodDTO.BANK){
            if(order.getPayment().getMethod() != PaymentMethod.SLIP_VERIFY_VIA_ACCOUNT){
                throw new PaymentMismatch();
            }

            data = this.paymentService.doProceedViaSlipVerifyByBankAccount(file.getBytes(), order);
        }else if(method == SlipMethodDTO.PROMPTPAY){
            if(order.getPayment().getMethod() != PaymentMethod.SLIP_VERIFY_VIA_PROMPTPAY){
                throw new PaymentMismatch();
            }

            data = this.paymentService.doProceedViaSlipVerifyByPromptpay(file.getBytes(), order);
        }else{
            throw new PaymentProceedFail("Invalid payment method.");
        }

        // Parse date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

        // Update payment
        PaymentEntity payment = order.getPayment();
        payment.setPaidAt(sdf.parse(data.getData().getTransDate() + " " + data.getData().getTransTime()));
        payment.setTransactionId(data.getDiscriminator());
        payment.setMetadata(gson.toJson(data));
        // Update payment
        this.paymentService.updatePayment(payment);
        // Push event to complete
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

        if(order.getPayment().getMethod() != PaymentMethod.TRUEMONEY_ENVELOP){
            throw new PaymentMismatch();
        }

        // Proceed this process
        TmnVoucherResponse tmn = this.paymentService.doProceedViaTruemoneyVoucher(payload.voucherUrl(), order);

        // Create entity
        PaymentEntity payment = order.getPayment();
        payment.setPaidAt(new Date(tmn.getData().getMyTicket().updateDate));
        payment.setTransactionId(tmn.getData().getVoucher().voucherId);
        payment.setMetadata(gson.toJson(tmn));
        // Update payment
        this.paymentService.updatePayment(payment);
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

        // If existed. Check if payment method is promptpay
        if(payment.getMethod() != PaymentMethod.PROMPTPAY){
            throw new PaymentMismatch();
        }

        // Create response
        PaymentPromtpayResultDTO response = new PaymentPromtpayResultDTO();
        response.setAmount(payment.getAmount());

        if(payment.getQrCode() == null){
            // Generate transaction ID
            Ulid uid = UlidCreator.getUlid();
            String transactionId = uid.toString().substring(6);
            // Set transaction id
            payment.setTransactionId(transactionId);

            PromptpayCreateResult ppResult = this.paymentService.generatePromtpayQRCodeBySCB(payment);

            if(ppResult == null){
                throw new PaymentProceedFail("Create QR code failed. Please try again.");
            }

            // Get QRCode
            String ppRaw = ppResult.getData().getQrRawData();

            // Update QRCode
            payment.setQrCode(ppRaw);
            // Update payment
            this.paymentService.updatePayment(payment);

            response.setQrCode(ppRaw);
            return response;
        }else{
            response.setQrCode(payment.getQrCode());
        }

        return response;
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

        // Get payment
        PaymentEntity payment = order.getPayment();

        // If existed. Check if payment method is promptpay
        if(payment.getMethod() != PaymentMethod.TRUEMONEY_APP){
            throw new PaymentMismatch();
        }

        // Generate transaction ID
        Ulid uid = UlidCreator.getUlid();
        String transactionId = uid.toString().substring(6);
        // Set transaction id
        payment.setTransactionId(transactionId);

        PaymentCreateURLResult payResult = this.paymentService.generateTruemoneyPaymentURLByChillPay(payment);

        if(payResult == null){
            throw new PaymentProceedFail("Create payment URL failed. Please try again.");
        }

        // Create entity
        payment.setRef1(String.valueOf(payResult.TransactionId));
        payment.setRef2(payResult.Token);
        // Update payment first
        this.paymentService.updatePayment(payment);

        return new PaymentURLResultDTO(payResult.getPaymentUrl());
    }
}
