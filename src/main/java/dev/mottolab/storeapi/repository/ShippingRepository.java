package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingRepository extends JpaRepository<ShippingEntity, Integer> {
    Optional<ShippingEntity> findByTrackingNumber(String trackingId);
}
