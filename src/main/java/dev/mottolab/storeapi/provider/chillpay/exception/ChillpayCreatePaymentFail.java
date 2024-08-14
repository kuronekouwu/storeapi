package dev.mottolab.storeapi.provider.chillpay.exception;

public class ChillpayCreatePaymentFail extends Throwable {
    private Integer code = 200;

    public ChillpayCreatePaymentFail(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
