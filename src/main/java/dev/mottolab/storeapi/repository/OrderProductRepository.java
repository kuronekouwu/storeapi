package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Integer> {
    List<OrderProductEntity> findAllByOrderId(UUID orderId);
}
