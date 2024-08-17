package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.callback.ThailandPostCallback;
import dev.mottolab.storeapi.dto.response.shipping.ShippingRateDTO;
import dev.mottolab.storeapi.entity.ShippingEntity;
import dev.mottolab.storeapi.entity.ShippingTrackerEntity;
import dev.mottolab.storeapi.service.ShippingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping("/rate")
    public List<ShippingRateDTO> getShippingRate() {
        return shippingService.getShippingRate().stream().map(ShippingRateDTO::new).toList();
    }

    @PostMapping("/callback/thailandPost")
    public void callbackThailandPost(
            @RequestBody ThailandPostCallback payload
    ) {
        for(ThailandPostCallback.ThailandPostCallbackItem data : payload.getItems()){
            String trackingNo =  data.getBarcode();
            // Search tracking number
            Optional<ShippingEntity> shipping =  shippingService.getShippingByTrackingId(trackingNo);
            if(shipping.isPresent()){
                ShippingTrackerEntity shippingTrackerEntity = getShippingTrackerEntity(data, shipping);
                shippingService.updateShippingTracker(shippingTrackerEntity);
            }
        }
    }

    private static @NotNull ShippingTrackerEntity getShippingTrackerEntity(ThailandPostCallback.ThailandPostCallbackItem data, Optional<ShippingEntity> shipping) {
        ShippingEntity shippingEntity = shipping.get();
        // New entity
        ShippingTrackerEntity shippingTrackerEntity = new ShippingTrackerEntity();
        shippingTrackerEntity.setShipping(shippingEntity);
        shippingTrackerEntity.setStatus(data.getStatus());
        shippingTrackerEntity.setDescription(data.getStatusDescription());
        shippingTrackerEntity.setLocation(data.getLocation());
        shippingTrackerEntity.setReceiveName(data.getReceiverName());
        return shippingTrackerEntity;
    }
}
