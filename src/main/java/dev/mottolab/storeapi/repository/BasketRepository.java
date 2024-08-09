package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.BasketEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BasketRepository extends JpaRepository<BasketEntity, Integer>  {
    List<BasketEntity> findAlLByUserId(Integer customerId);
    Optional<BasketEntity> findByUserIdAndProductId(Integer userId, UUID productId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<BasketEntity> findAllByIdIn(List<Integer> ids);
}
