package dev.mottolab.storeapi.exception;

public class FileRequireImage extends RuntimeException {
    public FileRequireImage() {
        super("Please upload image file.");
    }
}
