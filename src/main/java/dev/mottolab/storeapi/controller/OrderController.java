package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.order.CreateOrderByBasketIdDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @PostMapping("/user/basket/createDraftOrder")
    public void creteOrderByBasketIds(
            @Valid @RequestBody List<CreateOrderByBasketIdDTO> payload
    ){}

    @PostMapping("/user/topup/createOrder")
    public void creteOrderByTopup(){}

    @GetMapping("/user/checkOrder/{id}")
    public void checkOrderStatus(@PathVariable UUID id){}


}
