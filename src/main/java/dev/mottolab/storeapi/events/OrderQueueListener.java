package dev.mottolab.storeapi.events;

import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Configuration
public class OrderQueueListener implements ApplicationListener<OrderQueueEvent> {

    private final OrderService orderService;

    public OrderQueueListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @Transactional
    @Override
    public void onApplicationEvent(OrderQueueEvent event) {
        // Get payment ID
        UUID paymentId = event.getPaymentEntity().getId();

        // Get order
        Optional<OrderEntity> order = this.orderService.getOrderByPaymentId(paymentId);
        if(order.isPresent()) {
            // Update to complete
            OrderEntity orderEntity = order.get();
            orderEntity.setStatus(OrderStatus.SUCCESS);
            this.orderService.updateOrder(orderEntity);

            log.info("Updated order status: {} (Order ID: {})", orderEntity.getStatus(), orderEntity.getId().toString());
        }else{
            log.warn("Order does not exist");
        }
    }
}
