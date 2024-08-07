package dev.mottolab.storeapi.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record ProductUpdateDTO(
        @NotEmpty
        @NotNull
        @Length(min = 1, max = 64)
        String name,
        String description,
        @NotNull
        @Min(1)
        Double price,
        @Min(1)
        @JsonProperty("category")
        UUID categoryId
) {
}
