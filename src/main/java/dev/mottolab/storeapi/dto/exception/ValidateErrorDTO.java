package dev.mottolab.storeapi.dto.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidateErrorDTO {
    private final List<ValidateFieldErrorDTO> errors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        errors.add(new ValidateFieldErrorDTO(field, message));
    }
}
