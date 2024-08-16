package dev.mottolab.storeapi.exception;

public class AddressNotExist extends RuntimeException {
    public AddressNotExist() {
        super("Address not exist");
    }
}
