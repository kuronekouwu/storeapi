package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.basket.AddBasketDTO;
import dev.mottolab.storeapi.dto.response.baskets.BasketDTO;
import dev.mottolab.storeapi.entity.BasketEntity;
import dev.mottolab.storeapi.entity.ProductEntity;
import dev.mottolab.storeapi.entity.UserInfoEntity;
import dev.mottolab.storeapi.exception.ProductNotExist;
import dev.mottolab.storeapi.service.BasketService;
import dev.mottolab.storeapi.service.ProductService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/baskets")
public class BasketController {
    private final BasketService basketService;
    private final ProductService productService;

    public BasketController(
            BasketService basketService,
            ProductService productService
    ) {
        this.basketService = basketService;
        this.productService = productService;
    }

    @GetMapping
    @ResponseBody
    public List<BasketDTO> getAllBasket(@AuthenticationPrincipal UserInfoDetail user){
        // Load baskets
        List<BasketEntity> basketEntities =  this.basketService.getAllBasketByUserId(user.getUserId());
        return basketEntities
                .stream()
                .map(BasketDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createBasket(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody AddBasketDTO payload
    ) throws ProductNotExist {
        // Get product
        Optional<ProductEntity> productEntity = this.productService.getProductById(UUID.fromString(payload.productId()));
        if(productEntity.isEmpty()){
            throw new ProductNotExist();
        }

        // Check basket has exists
        Optional<BasketEntity> basket = this.basketService.getBasketByUserIdAndProductId(user.getUserId(), productEntity.get().getId());
        if(basket.isEmpty()){
            // New user
            UserInfoEntity userEntity = new UserInfoEntity();
            userEntity.setId(user.getUserId());

            BasketEntity entity = new BasketEntity();
            entity.setProduct(productEntity.get());
            entity.setUser(userEntity);

            // Save it
            this.basketService.updateBasket(entity);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
