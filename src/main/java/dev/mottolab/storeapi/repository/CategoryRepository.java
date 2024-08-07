package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findBySlug(String name);
    Integer countBySlugContaining(String slug);
}
