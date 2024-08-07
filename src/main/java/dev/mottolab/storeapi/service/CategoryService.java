package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.enitity.CategoryEntity;
import dev.mottolab.storeapi.repository.CategoryRepository;
import dev.mottolab.storeapi.service.utils.SlugService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public CategoryEntity updateCategory(CategoryEntity entity) {
        return this.repo.save(entity);
    }

    public List<CategoryEntity> getAllCategory(Integer page, Integer size) {
        return this.repo.findAll(PageRequest.of(page, size)).getContent();
    }

    public Optional<CategoryEntity> getCategoryById(Integer id){
        return this.repo.findById(id);
    }

    public Optional<CategoryEntity> getProductBySlug(String slug){
        return this.repo.findBySlug(slug);
    }

    public void deleteCategoryById(Integer id) {
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
