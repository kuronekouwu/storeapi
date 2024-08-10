package dev.mottolab.storeapi.exception;

public class OrderNotExist extends RuntimeException {
    public OrderNotExist() {
        super("Order not exist");
    }
}
