package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.ShippingEntity;
import dev.mottolab.storeapi.entity.ShippingRateEntity;
import dev.mottolab.storeapi.entity.ShippingTrackerEntity;
import dev.mottolab.storeapi.repository.ShippingAddressRepository;
import dev.mottolab.storeapi.repository.ShippingRateRepository;
import dev.mottolab.storeapi.repository.ShippingRepository;
import dev.mottolab.storeapi.repository.ShippingTrackerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingService {
    private final ShippingRepository shippingRepository;
    private final ShippingTrackerRepository shippingTrackerRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingRateRepository shippingRateRepository;

    public ShippingService(
            ShippingAddressRepository shippingAddressRepository,
            ShippingRateRepository shippingRateRepository,
            ShippingRepository shippingRepository,
            ShippingTrackerRepository shippingTrackerEntity
    ) {

        this.shippingAddressRepository = shippingAddressRepository;
        this.shippingRateRepository = shippingRateRepository;
        this.shippingRepository = shippingRepository;
        this.shippingTrackerRepository = shippingTrackerEntity;
    }

    public void updateShipping(ShippingEntity entity){
        shippingAddressRepository.save(entity);
    }

    public void updateShippingTracker(ShippingTrackerEntity entity){
        shippingTrackerRepository.save(entity);
    }

    public List<ShippingRateEntity> getShippingRate(){
        return shippingRateRepository.findAll();
    }

    public Optional<ShippingRateEntity> getShippingRateById(int id){
        return shippingRateRepository.findById(id);
    }

    public Optional<ShippingEntity> getShippingByTrackingId(String trackingId){
        return this.shippingRepository.findByTrackingNumber(trackingId);
    }
}
