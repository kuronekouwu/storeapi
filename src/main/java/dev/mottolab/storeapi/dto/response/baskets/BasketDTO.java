package dev.mottolab.storeapi.dto.response.baskets;

import dev.mottolab.storeapi.dto.response.product.ProductDTO;
import dev.mottolab.storeapi.entity.BasketEntity;
import lombok.Getter;

@Getter
public class BasketDTO {
    private final Integer id;
    private final ProductDTO product;

    public BasketDTO(BasketEntity basket) {
        this.id = basket.getId();
        this.product = new ProductDTO(basket.getProduct());
    }
}
