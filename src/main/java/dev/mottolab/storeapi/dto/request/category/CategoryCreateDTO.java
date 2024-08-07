package dev.mottolab.storeapi.dto.request.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CategoryCreateDTO(
        @NotEmpty
        @NotNull
        @Length(min = 1, max = 64)
        String name,
        String description
) {
}
