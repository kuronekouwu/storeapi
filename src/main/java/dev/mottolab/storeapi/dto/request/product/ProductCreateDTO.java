package dev.mottolab.storeapi.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.validator.UUIDValidator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


public record ProductCreateDTO(
        @NotEmpty
        @NotNull
        @Length(min = 1, max = 64)
        String name,
        String description,
        @NotNull
        @Min(1)
        Double price,
        @UUIDValidator
        @JsonProperty("category")
        String categoryId
) {
}
