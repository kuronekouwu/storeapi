package dev.mottolab.storeapi.exception;

public class SlipNotValid extends RuntimeException {
    public SlipNotValid() {
        super("Slip has not valid");
    }
}