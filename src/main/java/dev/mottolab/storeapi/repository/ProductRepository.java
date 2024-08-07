package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.enitity.ProductEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findBySlug(String slug);
    Integer countBySlugContaining(String slug);
    List<ProductEntity> findAllByCategoryId(Integer id, PageRequest pageRequest);
}
