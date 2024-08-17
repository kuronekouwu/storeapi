package dev.mottolab.storeapi.entity.payment;


public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    EXPIRED, // หากเวลาชำระเงินหมดอายุ
    REFUNDING,
    REFUNDED;
}
