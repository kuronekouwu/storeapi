package dev.mottolab.storeapi.dto.request.payment;

import lombok.Getter;

public enum SlipMethodDTO {
    PROMPTPAY,
    BANK;

    @Getter
    private String value;
}
