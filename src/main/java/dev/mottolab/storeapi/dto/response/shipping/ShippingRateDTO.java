package dev.mottolab.storeapi.dto.response.shipping;

import dev.mottolab.storeapi.entity.ShippingRateEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingRateDTO {
    private int id;
    private String name;
    private String description;
    private Double amount;

    public ShippingRateDTO(ShippingRateEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.amount = entity.getPrice();
    }
}
