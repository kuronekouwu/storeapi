package dev.mottolab.storeapi.exception;

public class PaymentMismatch extends RuntimeException {
    public PaymentMismatch() {
        super("Payment mismatch");
    }
}
