package dev.mottolab.storeapi.events;

import dev.mottolab.storeapi.entity.PaymentEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OrderQueueEvent extends ApplicationEvent {
    private PaymentEntity paymentEntity;

    public OrderQueueEvent(Object source, PaymentEntity paymentEntity) {
        super(source);
        this.paymentEntity = paymentEntity;
    }
}
