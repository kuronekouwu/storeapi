package dev.mottolab.storeapi.exception;

public class ProductNotExist extends RuntimeException {
    public ProductNotExist() {
        super("Product not exist");
    }
}
