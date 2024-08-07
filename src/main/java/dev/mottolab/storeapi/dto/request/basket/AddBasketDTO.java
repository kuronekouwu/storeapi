package dev.mottolab.storeapi.dto.request.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.validator.UUIDValidator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddBasketDTO(
        @UUIDValidator
        @NotNull
        @NotEmpty
        @JsonProperty("product_id")
        String productId
) {
}
