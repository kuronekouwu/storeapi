package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.ShippingTrackerEntity;
import dev.mottolab.storeapi.repository.ShippingTrackerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackingService {
    private final ShippingTrackerRepository shippingTrackerRepository;

    public TrackingService(ShippingTrackerRepository shippingTrackerRepository) {
        this.shippingTrackerRepository = shippingTrackerRepository;
    }

    public List<ShippingTrackerEntity> getAllTrackingByShippingId(int shippingId) {
        return shippingTrackerRepository.findAllByShippingId(shippingId);
    }
}
