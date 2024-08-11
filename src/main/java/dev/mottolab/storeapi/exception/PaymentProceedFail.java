package dev.mottolab.storeapi.exception;

public class PaymentProceedFail extends RuntimeException {
    public PaymentProceedFail(String message) {
        super(message);
    }
}
