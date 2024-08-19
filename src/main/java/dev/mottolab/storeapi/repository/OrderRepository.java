package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.OrderEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByUserIdOrderByCreatedAtDesc(Integer userId, PageRequest page);
    Optional<OrderEntity> findByUserIdAndId(Integer userId, UUID orderId);
    Optional<OrderEntity> findByPaymentId(UUID paymentId);
}
