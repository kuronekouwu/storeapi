package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.enitity.ProductEnitity;
import dev.mottolab.storeapi.repository.ProductRepository;
import dev.mottolab.storeapi.service.utils.SlugService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo){
        this.repo = repo;
    }

    public ProductEnitity updateProduct(ProductEnitity product){
        return this.repo.save(product);
    }

    public List<ProductEnitity> getAllProducts(Integer page, Integer size){
        return this.repo.findAll(PageRequest.of(page, size)).getContent();
    }

    public List<ProductEnitity> getAllProducts(Integer page, Integer size, Integer category){
        return this.repo.findAllByCategoryId(category, PageRequest.of(page, size));
    }

    public Optional<ProductEnitity> getProductById(UUID id){
       return this.repo.findById(id);
    }

    public Optional<ProductEnitity> getProductBySlug(String slug){
        return this.repo.findBySlug(slug);
    }

    public void deleteProduct(UUID id){
        this.repo.deleteById(id);
    }

    public String createSlugName(String name){
        String slugName = SlugService.toSlug(name);
        // Check if slug exist
        if(getProductBySlug(slugName).isPresent()){
            // Get total slug
            Integer countTotal = this.repo.countBySlugContaining(slugName);
            return slugName + "-" + Integer.toString(countTotal+1, 36);
        }

        return slugName;
    }
}
