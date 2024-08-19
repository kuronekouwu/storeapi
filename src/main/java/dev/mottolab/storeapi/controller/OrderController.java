package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.order.CreateOrderByBasketIdDTO;
import dev.mottolab.storeapi.dto.request.tracking.CreateTrackingWithBarcodeDTO;
import dev.mottolab.storeapi.dto.response.order.OrderDTO;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.ShippingEntity;
import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.exception.OrderNotExist;
import dev.mottolab.storeapi.service.OrderService;
import dev.mottolab.storeapi.service.ShippingService;
import dev.mottolab.storeapi.service.TrackingService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final TrackingService trackingService;
    private final ShippingService shippingService;

    public OrderController(
            OrderService orderService,
            TrackingService trackingService,
            ShippingService shippingService
    ) {
        this.orderService = orderService;
        this.trackingService = trackingService;
        this.shippingService = shippingService;
    }

    @PostMapping("/user/basket/createOrder")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrderDTO creteOrderByBasketIds(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody CreateOrderByBasketIdDTO payload
    ){
        if(payload.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please select at least one item");
        }

        OrderEntity order = this.orderService.createOrder(payload, user);
        return new OrderDTO(
                order,
                order.getPayment(),
                this.orderService.getOrderProductsByOrderId(order.getId()),
                null
        );
    }

    @GetMapping("/user/getList")
    @ResponseBody
    public List<OrderDTO> checkOrderStatus(
            @AuthenticationPrincipal UserInfoDetail user,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(20) Integer size
    ){
        List<OrderDTO> result = new ArrayList<>();
        List<OrderEntity> orderList = this.orderService.getAllOrders(user.getUserId(), page, size);
        for(OrderEntity order : orderList){
            result.add(new OrderDTO(
                    order,
                    null,
                    this.orderService.getOrderProductsByOrderId(order.getId()),
                    null
            ));
        }

        return result;
    }

    @GetMapping("/user/checkOrder/{id}")
    @ResponseBody
    public OrderDTO checkOrderStatus(
            @AuthenticationPrincipal UserInfoDetail user,
            @PathVariable UUID id
    ) {
        OrderEntity order = this.orderService.getOrder(user.getUserId(), id)
                .orElseThrow(OrderNotExist::new);

        return new OrderDTO(
                order,
                order.getPayment(),
                this.orderService.getOrderProductsByOrderId(order.getId()),
                this.trackingService.getAllTrackingByShippingId(order.getShipping().getId())
        );
    }

    @PreAuthorize("hasAuthority('ORDER_TRACKING_CREATE')")
    @PostMapping("/admin/initTracking/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void updateTrackingOrder(
            @PathVariable UUID id,
            @Valid @RequestBody CreateTrackingWithBarcodeDTO payload
            ) {
        OrderEntity order = this.orderService.getOrder(id)
                .orElseThrow(OrderNotExist::new);

        if(order.getStatus() != OrderStatus.PREPARING){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is not preparing");
        }

        // Subscribe barcode
        boolean result = this.trackingService.subscribeTrackingByBarcode(payload.trackingId());
        if(!result){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tracking barcode not found. Please check your tracking barcode and try again");
        }

        // Update shipping
        ShippingEntity shipping = order.getShipping();
        shipping.setTrackingNumber(payload.trackingId());
        this.shippingService.updateShipping(shipping);
        // Change status preparing to shipping
        order.setStatus(OrderStatus.SHIPPING);
        orderService.updateOrder(order);
    }
}
