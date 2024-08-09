package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.order.CreateOrderByBasketIdDTO;
import dev.mottolab.storeapi.dto.response.order.OrderResponseCreatedDTO;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.service.OrderService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/user/basket/createDraftOrder")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrderResponseCreatedDTO creteOrderByBasketIds(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody CreateOrderByBasketIdDTO payload
    ){
        OrderEntity order = this.orderService.createOrder(payload, user);
        return new OrderResponseCreatedDTO(order);
    }

    @GetMapping("/user/checkOrder/{id}")
    public void checkOrderStatus(@PathVariable UUID id){

    }


}
