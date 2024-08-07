package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.enitity.ProductEnitity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEnitity, UUID> {
    Optional<ProductEnitity> findBySlug(String slug);
    Integer countBySlugContaining(String slug);
    List<ProductEnitity> findAllByCategoryId(Integer id, PageRequest pageRequest);
}
