package dev.mottolab.storeapi.dto.response.address;

import dev.mottolab.storeapi.entity.AddressDistrictsEntity;
import lombok.Getter;

@Getter
public class DistrictDTO {
    private int id;
    private String name;

    public DistrictDTO(AddressDistrictsEntity entity) {
        this.id = entity.getId();
        this.name = entity.getNameTH();
    }
}
