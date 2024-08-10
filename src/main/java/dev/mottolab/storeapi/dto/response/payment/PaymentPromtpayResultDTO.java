package dev.mottolab.storeapi.dto.response.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentPromtpayResultDTO {
    @JsonProperty("qrcode")
    private String qrCode;

    public PaymentPromtpayResultDTO(String qrCode) {
        this.qrCode = qrCode;
    }
}
