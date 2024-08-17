package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.ShippingRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingRateRepository extends JpaRepository<ShippingRateEntity, Integer> {
}
