package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.ShippingEntity;
import dev.mottolab.storeapi.entity.ShippingRateEntity;
import dev.mottolab.storeapi.repository.ShippingAddressRepository;
import dev.mottolab.storeapi.repository.ShippingRateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingService {
    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingRateRepository shippingRateRepository;

    public ShippingService(
            ShippingAddressRepository shippingAddressRepository,
            ShippingRateRepository shippingRateRepository
    ) {

        this.shippingAddressRepository = shippingAddressRepository;
        this.shippingRateRepository = shippingRateRepository;
    }

    public void updateShipping(ShippingEntity entity){
        shippingAddressRepository.save(entity);
    }

    public List<ShippingRateEntity> getShippingRate(){
        return shippingRateRepository.findAll();
    }

    public Optional<ShippingRateEntity> getShippingRateById(int id){
        return shippingRateRepository.findById(id);
    }
}
