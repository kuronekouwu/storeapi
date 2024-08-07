package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.events.OrderQueueEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final ApplicationEventPublisher event;

    public PaymentController(ApplicationEventPublisher applicationEventPublisher) {
        this.event = applicationEventPublisher;
    }

    @PostMapping("/slip/doProceed")
    public void doProceedByVerifiySlip(){
        this.event.publishEvent(new OrderQueueEvent(this));
    }

    @PostMapping("/voucher/doProceed")
    public void doProceedByTmnVoucher(){
        this.event.publishEvent(new OrderQueueEvent(this));
    }

    @PostMapping("/promptpay/createPayment")
    public void doCreatePaymentByPromptpay(){
        this.event.publishEvent(new OrderQueueEvent(this));
    }

    @PostMapping("/truemoney/createPayment")
    public void doCreatePaymentByTmn(){
        this.event.publishEvent(new OrderQueueEvent(this));
    }
}
