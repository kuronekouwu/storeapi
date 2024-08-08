package dev.mottolab.storeapi.entity.payment;

import lombok.Getter;

public enum PaymentMethod {
    SLIP_VERIFY,
    TRUEMONEY_ENVLOP,
    PROMPTPAY,
    TRUEMONEY_APP;

    @Getter
    private String paymentMethod;
}
