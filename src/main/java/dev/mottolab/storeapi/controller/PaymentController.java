package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.payment.GeneratePaymentDTO;
import dev.mottolab.storeapi.dto.request.payment.ProceedSlipVerifyDTO;
import dev.mottolab.storeapi.dto.request.payment.ProceedTmnVoucherDTO;
import dev.mottolab.storeapi.dto.response.payment.PaymentPromtpayResultDTO;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import dev.mottolab.storeapi.exception.OrderAlreadyProceed;
import dev.mottolab.storeapi.exception.OrderNotExist;
import dev.mottolab.storeapi.exception.PaymentCreateFail;
import dev.mottolab.storeapi.exception.PaymentMismatch;
import dev.mottolab.storeapi.provider.scbopenapi.response.PromptpayCreateResult;
import dev.mottolab.storeapi.service.OrderService;
import dev.mottolab.storeapi.service.PaymentService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/slip/promptpay/createQrCode")
    public void generateSlipQrCode(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody GeneratePaymentDTO payload
    ) throws OrderNotExist, PaymentCreateFail, PaymentMismatch, OrderAlreadyProceed {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                UUID.fromString(payload.orderId())
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }



        // TODO: Implement this method
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented");
    }

    @GetMapping("/slip/banking/getInformation")
    public void getBankInformation(){
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented");
    }

    @PostMapping("/slip/doProceed")
    public void doProceedByVerifiySlip(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody ProceedSlipVerifyDTO payload
    ) throws OrderNotExist, PaymentCreateFail, PaymentMismatch, OrderAlreadyProceed {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                UUID.fromString(payload.orderId())
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }

        // TODO: Implement this method
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented");
    }

    @PostMapping("/voucher/doProceed")
    public void doProceedByTmnVoucher(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody ProceedTmnVoucherDTO payload
    ) throws OrderNotExist, PaymentCreateFail, OrderAlreadyProceed {
        OrderEntity order = this.orderService.getOrder(
                user.getUserId(),
                UUID.fromString(payload.orderId())
        ).orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PENDING){
            throw new OrderAlreadyProceed();
        }

        // TODO: Implement this method
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented");
    }

    @PostMapping("/promptpay/createPayment")
    public PaymentPromtpayResultDTO doCreatePaymentByPromptpay(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody GeneratePaymentDTO payload
    ) throws OrderNotExist, PaymentCreateFail, PaymentMismatch, OrderAlreadyProceed {
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
            UUID uid = UUID.randomUUID();
            String transactionId = Long.toString(uid.getMostSignificantBits(), 36).toUpperCase();

            PromptpayCreateResult ppResult = this.paymentService.generatePromtpayQRCode(order, transactionId);

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
    public void doCreatePaymentByTmn(){
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented");
    }
}
