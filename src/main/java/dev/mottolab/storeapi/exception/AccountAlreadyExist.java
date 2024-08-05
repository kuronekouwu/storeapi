package dev.mottolab.storeapi.exception;

public class AccountAlreadyExist extends Throwable {
    private String account;

    public AccountAlreadyExist(String account) {
        super();
        this.account = account;
    }

    public String getAccount() {
        return account;
    }
}
