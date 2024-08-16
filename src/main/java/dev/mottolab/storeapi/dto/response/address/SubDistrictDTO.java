package dev.mottolab.storeapi.dto.response.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.AddressSubDistrictsEntity;
import lombok.Getter;

@Getter
public class SubDistrictDTO {
    private int id;
    private String name;
    @JsonProperty("zip_code")
    private int zipCode;

    public SubDistrictDTO(AddressSubDistrictsEntity entity) {
        this.id = entity.getId();
        this.name = entity.getNameTH();
        this.zipCode = entity.getZipCode();
    }
}
