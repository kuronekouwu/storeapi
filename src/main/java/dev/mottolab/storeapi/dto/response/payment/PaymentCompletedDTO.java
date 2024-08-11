package dev.mottolab.storeapi.dto.response.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.PaymentEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCompletedDTO {
    @JsonProperty("payment_id")
    private String paymentId;
    private Double amount;
    private String currency;

    public PaymentCompletedDTO(PaymentEntity payment) {
        this.paymentId = payment.getId().toString();
        this.amount = payment.getAmount();
        this.currency = "THB";
    }
}
