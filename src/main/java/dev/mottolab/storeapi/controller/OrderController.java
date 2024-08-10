package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.order.CreateOrderByBasketIdDTO;
import dev.mottolab.storeapi.dto.response.order.OrderResponseDTO;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.exception.OrderNotExist;
import dev.mottolab.storeapi.service.OrderService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
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

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/user/basket/createOrder")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrderResponseDTO creteOrderByBasketIds(
            @AuthenticationPrincipal UserInfoDetail user,
            @Valid @RequestBody CreateOrderByBasketIdDTO payload
    ){
        if(payload.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please select at least one item");
        }

        OrderEntity order = this.orderService.createOrder(payload, user);
        return new OrderResponseDTO(
                order,
                this.orderService.getOrderProductsByOrderId(order.getId())
        );
    }

    @GetMapping("/user/getList")
    @ResponseBody
    public List<OrderResponseDTO> checkOrderStatus(
            @AuthenticationPrincipal UserInfoDetail user,
            @RequestParam @Min(0) Integer page,
            @RequestParam @Min(1) @Max(20) Integer size
    ){
        List<OrderResponseDTO> result = new ArrayList<>();
        List<OrderEntity> orderList = this.orderService.getAllOrders(user.getUserId(), page, size);
        for(OrderEntity order : orderList){
            result.add(new OrderResponseDTO(order, this.orderService.getOrderProductsByOrderId(order.getId())));
        }

        return result;
    }

    @GetMapping("/user/checkOrder/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public OrderResponseDTO checkOrderStatus(
            @AuthenticationPrincipal UserInfoDetail user,
            @PathVariable UUID id
    ) throws OrderNotExist {
        OrderEntity order = this.orderService.getOrder(user.getUserId(), id)
                .orElseThrow(OrderNotExist::new);

        return new OrderResponseDTO(
                order,
                this.orderService.getOrderProductsByOrderId(order.getId())
        );
    }
}
