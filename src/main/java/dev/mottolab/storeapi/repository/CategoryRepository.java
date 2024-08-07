package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.enitity.CategoryEnitity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEnitity, Integer> {
    Optional<CategoryEnitity> findBySlug(String name);
    Integer countBySlugContaining(String slug);
}
