package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.enitity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findBySlug(String name);
    Integer countBySlugContaining(String slug);
}
