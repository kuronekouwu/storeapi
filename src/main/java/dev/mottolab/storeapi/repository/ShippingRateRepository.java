package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.ShippingRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRateRepository extends JpaRepository<ShippingRateEntity, Integer> {
}
