package dev.mottolab.storeapi.controller;

import com.google.gson.Gson;
import dev.mottolab.storeapi.dto.request.callback.ChillpayCompletedPayment;
import dev.mottolab.storeapi.dto.request.callback.SCBCompletedPaymentDTO;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/payment/callback")
public class PaymentCallbackController {
    private final PaymentService paymentService;
    private final Gson gson = new Gson();
    private String[] checksumPaymentCompletedField = {
            "TransactionId",
            "Amount",
            "OrderNo",
            "CustomerId",
            "BankCode",
            "PaymentDate",
            "PaymentStatus",
            "BankRefCode",
            "CurrentDate",
            "CurrentTime",
            "PaymentDescription",
            "CreditCardToken",
            "Currency",
            "CustomerName"
    };

    public PaymentCallbackController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/scb/completed")
    public void doCompletedPaymentSCB(@RequestBody SCBCompletedPaymentDTO payload) {
        // Get billing ref
        String reference = payload.billPaymentRef1();
        // Search in database
        Optional<PaymentEntity> payment = this.paymentService.getPaymentByTransactionId(reference);
        if(payment.isPresent()) {
            // Update payment
            PaymentEntity paymentEntity = payment.get();
            paymentEntity.setMetadata(gson.toJson(payload));
            // Update order status
            paymentService.updatePayment(paymentEntity);
            // Push event
            this.paymentService.doCompletePayment(paymentEntity);
        }
    }

    @PostMapping(value = "/chillpay/completed", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doCompletedPaymentChillpay(ChillpayCompletedPayment payload) {
        if(!paymentService.verifyChecksumByChillpay(payload.toChecksumString(), payload.CheckSum)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid checksum");
        }

        // Search in database
        Optional<PaymentEntity> payment = this.paymentService.getPaymentByTransactionId(payload.OrderNo);
        if(payment.isPresent()) {
            // Update payment
            PaymentEntity paymentEntity = payment.get();
            paymentEntity.setMetadata(gson.toJson(payload));
            // Update order status
            paymentService.updatePayment(paymentEntity);
            // Push event
            this.paymentService.doCompletePayment(paymentEntity);
        }
    }
}
