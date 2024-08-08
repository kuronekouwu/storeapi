package dev.mottolab.storeapi.dto.request.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateOrderByBasketIdDTO(
        @NotNull
        @NotEmpty
        Integer id,
        @NotNull
        @NotEmpty
        @Min(1)
        Integer unit
) {
}
