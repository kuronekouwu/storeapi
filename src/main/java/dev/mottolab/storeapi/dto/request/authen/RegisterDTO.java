package dev.mottolab.storeapi.dto.request.authen;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterDTO(
        @NotEmpty
        @Email
        String account,
        @NotEmpty
        String password,
        @NotEmpty
        String display_name
) {
}
