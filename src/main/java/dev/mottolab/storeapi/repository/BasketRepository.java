package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BasketRepository extends JpaRepository<BasketEntity, Integer>  {
    List<BasketEntity> findAlLByUserId(int customerId);
    Optional<BasketEntity> findByUserIdAndProductId(Integer userId, UUID productId);
}
