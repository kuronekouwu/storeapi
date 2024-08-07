package dev.mottolab.storeapi.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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
        @Min(1)
        @JsonProperty("category")
        Integer categoryId
) {
}
