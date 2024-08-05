package dev.mottolab.storeapi.dto.exception;

public record ValidateFieldErrorDTO(
        String field,
        String message
) {
}
