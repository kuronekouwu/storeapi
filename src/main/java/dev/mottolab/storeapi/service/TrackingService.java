package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.ShippingEntity;
import dev.mottolab.storeapi.entity.ShippingTrackerEntity;
import dev.mottolab.storeapi.provider.thailandpost.ThailandPostProvider;
import dev.mottolab.storeapi.repository.ShippingTrackerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackingService {
    private final ShippingTrackerRepository shippingTrackerRepository;
    private final ThailandPostProvider thailandPostProvider;

    public TrackingService(
            ShippingTrackerRepository shippingTrackerRepository,
            ThailandPostProvider thailandPostProvider
    ) {
        this.shippingTrackerRepository = shippingTrackerRepository;
        this.thailandPostProvider = thailandPostProvider;
    }

    public List<ShippingTrackerEntity> getAllTrackingByShippingId(int shippingId) {
        return shippingTrackerRepository.findAllByShippingId(shippingId, Sort.by(Sort.Direction.DESC, "id"));
    }

    public boolean subscribeTrackingByBarcode(String barcode) {
        return thailandPostProvider.subscribeByBarcode(barcode);
    }
}
