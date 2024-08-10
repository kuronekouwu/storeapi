package dev.mottolab.storeapi.exception;

public class AccountAlreadyExist extends RuntimeException {
    public AccountAlreadyExist(String account) {
        super("Account: " + account + " already exist.");
    }
}
