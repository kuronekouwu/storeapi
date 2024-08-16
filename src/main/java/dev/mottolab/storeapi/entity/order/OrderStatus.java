package dev.mottolab.storeapi.entity.order;

import lombok.Getter;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    PREPARING,
    SHIPPING,
    COMPLETED,
    REFUNDED, // คืนเงิน ในกรณีสินค้าเสียหายหรือคืนของ
    CANCEL; // เกิดเคสยกเลิกสินค้า หรีอหมดเวลาชำระเงิน

    @Getter
    private String value;
}
