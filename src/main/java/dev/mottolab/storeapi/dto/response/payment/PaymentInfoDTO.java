package dev.mottolab.storeapi.dto.response.payment;

import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfoDTO {
    private PaymentMethod method;
    private Double amount;

    public PaymentInfoDTO(PaymentEntity payment) {
        this.method = payment.getMethod();
        this.amount = payment.getAmount();
    }
}
