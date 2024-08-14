package dev.mottolab.storeapi.exception;

public class QRCodeNotExist extends RuntimeException {
    public QRCodeNotExist() {
        super("QR Code Not Exist");
    }
}
