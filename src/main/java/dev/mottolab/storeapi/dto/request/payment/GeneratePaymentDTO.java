package dev.mottolab.storeapi.dto.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.validator.UUIDValidator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GeneratePaymentDTO(
        @NotNull
        @NotEmpty
        @UUIDValidator
        @JsonProperty("order_id")
        String orderId
) {
}
