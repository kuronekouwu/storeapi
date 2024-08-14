package dev.mottolab.storeapi.entity.payment;

import lombok.Getter;

public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    REFUNDING,
    REFUNDED;

    @Getter
    private String paymentStatus;
}
