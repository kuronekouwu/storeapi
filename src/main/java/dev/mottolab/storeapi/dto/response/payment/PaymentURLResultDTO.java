package dev.mottolab.storeapi.dto.response.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentURLResultDTO {
    private String payUrl;

    public PaymentURLResultDTO(String payUrl) {
        this.payUrl = payUrl;
    }
}
