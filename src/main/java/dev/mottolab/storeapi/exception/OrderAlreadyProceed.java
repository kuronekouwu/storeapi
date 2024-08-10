package dev.mottolab.storeapi.exception;

public class OrderAlreadyProceed extends RuntimeException {
    public OrderAlreadyProceed() {
        super("Order has already been proceed");
    }
}
