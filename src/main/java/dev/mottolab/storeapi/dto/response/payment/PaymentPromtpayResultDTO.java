package dev.mottolab.storeapi.dto.response.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentPromtpayResultDTO {
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("qrcode")
    private String qrCode;
}
