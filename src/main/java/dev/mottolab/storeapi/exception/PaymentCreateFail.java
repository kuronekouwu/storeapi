package dev.mottolab.storeapi.exception;

public class PaymentCreateFail extends RuntimeException {
    public PaymentCreateFail() {
        super("Create payment failed");
    }
}
