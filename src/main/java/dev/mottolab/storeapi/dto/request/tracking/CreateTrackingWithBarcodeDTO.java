package dev.mottolab.storeapi.dto.request.tracking;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record CreateTrackingWithBarcodeDTO(
        @JsonProperty("tracking_id")
        @NotEmpty
        String trackingId
) {
}
