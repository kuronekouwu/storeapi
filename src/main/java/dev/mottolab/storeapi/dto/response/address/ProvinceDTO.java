package dev.mottolab.storeapi.dto.response.address;

import dev.mottolab.storeapi.entity.AddressProvincesEntity;
import lombok.Getter;

@Getter
public class ProvinceDTO {
    private int id;
    private String name;

    public ProvinceDTO(AddressProvincesEntity entity) {
        this.id = entity.getId();
        this.name = entity.getNameTH();
    }
}
