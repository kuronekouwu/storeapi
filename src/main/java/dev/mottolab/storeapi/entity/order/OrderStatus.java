package dev.mottolab.storeapi.entity.order;

import lombok.Getter;

public enum OrderStatus {
    PENDING,
    SUCCESS,
    REFUNDED;

    @Getter
    private String paymentStatus;
}
