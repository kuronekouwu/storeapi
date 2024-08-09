package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Integer> {
}
