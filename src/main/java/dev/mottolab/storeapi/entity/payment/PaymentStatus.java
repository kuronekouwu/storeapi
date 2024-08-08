package dev.mottolab.storeapi.entity.payment;

import lombok.Getter;

public enum PaymentStatus {
    PENDING,
    SUCCESS,
    REFUNDED;

    @Getter
    private String paymentStatus;
}
