package dev.mottolab.storeapi.dto.response.product;

import dev.mottolab.storeapi.enitity.ProductEntity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductDTO {
    private final UUID id;
    private final String slug;
    private final String name;
    private final String description;
    private final Double price;
    private final String image;

    public ProductDTO(ProductEntity entity) {
        this.id = entity.getId();
        this.slug = entity.getSlug();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.image = getImage();
    }
}
