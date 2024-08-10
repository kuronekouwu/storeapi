package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.events.OrderQueueEvent;
import dev.mottolab.storeapi.exception.PaymentCreateFail;
import dev.mottolab.storeapi.provider.scbopenapi.SCBAPIProvider;
import dev.mottolab.storeapi.provider.scbopenapi.response.PaymentPromptpayCreateResult;
import dev.mottolab.storeapi.repository.PaymentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    private final ApplicationEventPublisher event;
    private final SCBAPIProvider scb;
    private final PaymentRepository repo;

    public PaymentService(
            ApplicationEventPublisher applicationEventPublisher,
            SCBAPIProvider scb,
            PaymentRepository repo
    ) {
        this.event = applicationEventPublisher;
        this.scb = scb;
        this.repo = repo;
    }

    public void doCompletePayment(PaymentEntity paymentEntity){
        this.event.publishEvent(new OrderQueueEvent(this, paymentEntity));
    }

    public PaymentPromptpayCreateResult generatePromtpayQRCode(OrderEntity order, String transactionId) throws PaymentCreateFail {
        // Create class
        SCBAPIProvider.GeneratePromptpayQrCode pp = new SCBAPIProvider.GeneratePromptpayQrCode();
        pp.setAmount(order.getTotal());
        pp.setRef1(transactionId);
        pp.setRef2(transactionId);

        // Generate payment
        PaymentPromptpayCreateResult ppResult =  this.scb.generatePromptpayQrCode(pp);
        if(ppResult == null){
            throw new PaymentCreateFail();
        }

        return ppResult;
    }

    public void updatePayment(PaymentEntity payment){
        this.repo.save(payment);
    }

    public Optional<PaymentEntity> getPaymentByTransactionId(String transactionId){
        return this.repo.findByTransactionId(transactionId);
    }
}
