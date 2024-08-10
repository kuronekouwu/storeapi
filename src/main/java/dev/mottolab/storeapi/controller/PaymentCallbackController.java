package dev.mottolab.storeapi.controller;

import com.google.gson.Gson;
import dev.mottolab.storeapi.dto.request.callback.SCBCompletedPaymentDTO;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.service.OrderService;
import dev.mottolab.storeapi.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payment/callback")
public class PaymentCallbackController {
    private final PaymentService paymentService;
    private final Gson gson = new Gson();

    public PaymentCallbackController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/scb/completed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
}
