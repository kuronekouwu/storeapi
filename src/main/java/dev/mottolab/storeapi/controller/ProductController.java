package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.product.ProductCreateDTO;
import dev.mottolab.storeapi.dto.request.product.ProductUpdateDTO;
import dev.mottolab.storeapi.enitity.ProductEnitity;
import dev.mottolab.storeapi.exception.ProductNotExist;
import dev.mottolab.storeapi.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ProductEnitity createProduct(@Valid @RequestBody ProductCreateDTO product) {
        return productService.createProduct(product);
    }

    @GetMapping
    @ResponseBody
    public List<ProductEnitity> getProducts(@RequestParam @Min(0) Integer page, @RequestParam @Min(1) @Max(100) Integer size) {
        return this.productService.getAllProducts(page, size);
    }

    @GetMapping("/{slug}")
    @ResponseBody
    public ProductEnitity getProduct(@PathVariable String slug) throws ProductNotExist {
        Optional<ProductEnitity> product = this.productService.getProductBySlug(slug);
        if(product.isEmpty()){
            throw new ProductNotExist();
        }

        return product.get();
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ProductEnitity updateProduct(
            @PathVariable UUID id,
            @RequestBody  ProductUpdateDTO product
    ) throws ProductNotExist {
        Optional<ProductEnitity> productEnitity = this.productService.getProductById(id);
        if(productEnitity.isEmpty()){
            throw new ProductNotExist();
        }

        ProductEnitity productEntity = productEnitity.get();
        productEntity.setName(product.name());
        productEntity.setDescription(product.description());
        productEntity.setPrice(product.price());
        return this.productService.updateProduct(productEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable UUID id
    ) throws ProductNotExist {
        Optional<ProductEnitity> productEnitity = this.productService.getProductById(id);
        if(productEnitity.isEmpty()){
            throw new ProductNotExist();
        }

        // Delete it
        this.productService.deleteProduct(productEnitity.get().getId());
    }
}
