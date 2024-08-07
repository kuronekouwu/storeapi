package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.category.CategoryCreateDTO;
import dev.mottolab.storeapi.dto.request.product.ProductUpdateDTO;
import dev.mottolab.storeapi.dto.response.category.CategoryDTO;
import dev.mottolab.storeapi.enitity.CategoryEnitity;
import dev.mottolab.storeapi.exception.CategoryNotExist;
import dev.mottolab.storeapi.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryEnitity createProduct(@Valid @RequestBody CategoryCreateDTO payload) {
        // Create entity
        CategoryEnitity entity = new CategoryEnitity();
        entity.setName(payload.name());
        entity.setDescription(payload.description());
        // Create slug
        entity.setSlug(this.categoryService.createSlugName(payload.name()));
        return categoryService.updateCategory(entity);
    }

    @GetMapping
    @ResponseBody
    public List<CategoryDTO> getCategories(
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(100) Integer size
    ) {
        return this.categoryService
                .getAllCategory(page, size)
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/getInfo/slug/{slug}")
    @ResponseBody
    public CategoryDTO getCategoryInfo(@PathVariable String slug) throws CategoryNotExist {
        Optional<CategoryEnitity> info = this.categoryService.getProductBySlug(slug);
        if(!info.isPresent()) {
            throw new CategoryNotExist();
        }

        return new CategoryDTO(info.get());
    }

    @PutMapping("/getInfo/id/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    public CategoryDTO updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductUpdateDTO payload
    ) throws CategoryNotExist {
        Optional<CategoryEnitity> enitity = this.categoryService.getCategoryById(id);
        if(enitity.isEmpty()){
            throw new CategoryNotExist();
        }

        CategoryEnitity entity = enitity.get();
        entity.setName(payload.name());
        entity.setDescription(payload.description());

        return new CategoryDTO(this.categoryService.updateCategory(entity));
    }

    @DeleteMapping("/getInfo/id/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable Integer id
    ) throws CategoryNotExist {
        Optional<CategoryEnitity> entity = this.categoryService.getCategoryById(id);
        if(entity.isEmpty()){
            throw new CategoryNotExist();
        }

        // Delete it
        this.categoryService.deleteCategoryById(entity.get().getId());
    }
}
