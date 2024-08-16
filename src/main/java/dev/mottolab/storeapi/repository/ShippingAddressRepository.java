package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingEntity, Integer> {
}
