package dev.mottolab.storeapi.exception;

public class ShippingRateNotExist extends RuntimeException {
    public ShippingRateNotExist() {
        super("Shipping rate not exist");
    }
}
