package dev.mottolab.storeapi.entity.order;

import lombok.Getter;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    PREPARING,
    SHIPPING,
    COMPLETED,
    REFUNDED,
    CANCEL;

    @Getter
    private String value;
}
