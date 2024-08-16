package dev.mottolab.storeapi.dto.response.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import dev.mottolab.storeapi.entity.payment.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfoDTO {
    private PaymentMethod method;
    private Double amount;
    private PaymentStatus status;
    @JsonProperty("paid_time")
    private Long paidTime;

    public PaymentInfoDTO(PaymentEntity payment) {
        this.method = payment.getMethod();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.paidTime = payment.getPaidAt() != null ? payment.getPaidAt().getTime() : null;
    }
}
