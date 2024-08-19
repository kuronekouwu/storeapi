package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.ShippingTrackerEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingTrackerRepository extends JpaRepository<ShippingTrackerEntity, Integer> {
    List<ShippingTrackerEntity> findAllByShippingId(int shippingId, Sort sort);
}
