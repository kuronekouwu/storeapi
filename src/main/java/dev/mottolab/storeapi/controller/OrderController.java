package dev.mottolab.storeapi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @PostMapping("/user/basket/createOrder")
    public void creteOrderByBasketIds(){}

    @PostMapping("/user/topup/createOrder")
    public void creteOrderByTopup(){}

    @GetMapping("/user/checkOrder/{id}")
    public void checkOrderStatus(@PathVariable int id){}


}
