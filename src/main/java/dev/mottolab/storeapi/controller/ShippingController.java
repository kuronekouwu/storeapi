package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.response.shipping.ShippingRateDTO;
import dev.mottolab.storeapi.service.ShippingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
