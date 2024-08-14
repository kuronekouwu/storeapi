package dev.mottolab.storeapi.provider.qrcr.exception;

public class QRCRError extends Throwable {
    private Integer code;

    public QRCRError(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
