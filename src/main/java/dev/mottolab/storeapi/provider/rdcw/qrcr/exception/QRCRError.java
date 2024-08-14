package dev.mottolab.storeapi.provider.rdcw.qrcr.exception;

public class QRCRError extends Throwable {
    private Integer code;

    public QRCRError(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
