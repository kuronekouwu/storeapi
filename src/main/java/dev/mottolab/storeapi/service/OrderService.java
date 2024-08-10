package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.dto.request.order.CreateOrderByBasketIdDTO;
import dev.mottolab.storeapi.entity.*;
import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.repository.OrderProductRepository;
import dev.mottolab.storeapi.repository.OrderRepository;
import dev.mottolab.storeapi.user.UserInfoDetail;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final BasketService basketService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(
            BasketService basketService,
            OrderRepository orderRepository,
            OrderProductRepository orderProductRepository,
            ProductService productService
    ) {
        this.basketService = basketService;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productService = productService;
    }

    @Transactional()
    public OrderEntity createOrder(CreateOrderByBasketIdDTO payload, UserInfoDetail user) {
        // Create order
        OrderEntity orderEntity = new OrderEntity();
        // Init new user
        UserInfoEntity userEntity = new UserInfoEntity();
        userEntity.setId(user.getUserId());
        orderEntity.setUser(userEntity);
        orderEntity.setStatus(OrderStatus.PENDING);
        // Create array list
        List<OrderProductEntity> orderProducts = new ArrayList<>();
        List<ProductEntity> products = new ArrayList<>();

        for(CreateOrderByBasketIdDTO.BasketList data : payload.getItems()) {
            Optional<BasketEntity> basket = this.basketService.getById(data.getId());

            if(basket.isPresent()){
                // Get one row
                BasketEntity basketEntity = basket.get();
                // Get stock
                ProductEntity product = basketEntity.getProduct();
                if(product.getStock() <= 0){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product has been out of stock.");
                }
                if(product.getStock() - data.getUnit() < 0){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product has insufficient stocks.");
                }

                // Create order product
                OrderProductEntity orderProductEntity = getOrderProductEntity(data, product, orderEntity);
                // Push data
                orderProducts.add(orderProductEntity);
                // Decrement stock product
                product.setStock(product.getStock() - data.getUnit());
                products.add(product);
                orderEntity.setTotal(orderEntity.getTotal() + (product.getPrice() * data.getUnit()));
            }
        }

        if(!orderProducts.isEmpty()){
            // Save order first
            this.updateOrder(orderEntity);
            // Save order product
            this.orderProductRepository.saveAll(orderProducts);
            // Update product
            this.productService.updateManyProduct(products);
            // Delete basket by id
            this.basketService.deleteAllById(
                    payload
                            .getItems()
                            .stream()
                            .map(CreateOrderByBasketIdDTO.BasketList::getId)
                            .toList()
            );

            return this.orderRepository.findById(orderEntity.getId()).get();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No product item found.");
    }

    public OrderEntity updateOrder(OrderEntity orderEntity) {
        return this.orderRepository.save(orderEntity);
    }

    private static OrderProductEntity getOrderProductEntity(
            CreateOrderByBasketIdDTO.BasketList data,
            ProductEntity product,
            OrderEntity orderEntity
    ) {
        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setProduct(product);
        orderProductEntity.setOrder(orderEntity);
        orderProductEntity.setQuantity(data.getUnit());
        // Get information product
        orderProductEntity.setProductName(product.getName());
        orderProductEntity.setPrice(product.getPrice());
        orderProductEntity.setProductImage(product.getImage());
        return orderProductEntity;
    }

    public List<OrderEntity> getAllOrders(Integer userId, Integer page, Integer size) {
        return this.orderRepository.findAllByUserId(userId, PageRequest.of(page, size));
    }

    public Optional<OrderEntity> getOrderByPaymentId(UUID paymentId) {
        return this.orderRepository.findByPaymentId(paymentId);
    }

    public Optional<OrderEntity> getOrder(UUID orderId) {
        return this.orderRepository.findById(orderId);
    }

    public Optional<OrderEntity> getOrder(Integer userId, UUID orderId) {
        return this.orderRepository.findByUserIdAndId(userId, orderId);
    }

    public List<OrderProductEntity> getOrderProductsByOrderId(UUID orderId) {
        return this.orderProductRepository.findAllByOrderId(orderId);
    }
}
