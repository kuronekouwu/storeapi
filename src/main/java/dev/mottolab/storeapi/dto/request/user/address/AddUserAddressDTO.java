package dev.mottolab.storeapi.dto.request.user.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddUserAddressDTO(
        @NotEmpty
        @JsonProperty("full_name")
        String fullName,
        @NotEmpty
        @JsonProperty("address_line_1")
        String addressLine1,
        @JsonProperty("address_line_2")
        String addressLine2,
        @Min(1)
        @NotNull
        @JsonProperty("address_id")
        Integer addressId,
        @NotEmpty
        @JsonProperty("mobile")
        String phoneNumber
) {
}
