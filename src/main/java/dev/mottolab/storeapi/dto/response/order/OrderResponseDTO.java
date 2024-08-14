package dev.mottolab.storeapi.dto.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.dto.response.payment.PaymentInfoDTO;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.OrderProductEntity;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.entity.ProductEntity;
import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.service.utils.UUIDService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO {
    @JsonProperty("order_id")
    private UUID orderId;
    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("result")
    private OrderStatus result;
    @JsonProperty("payment")
    private PaymentInfoDTO payment = null;
    @JsonProperty("items")
    private List<OrderProductResponseDTO> items;

    public OrderResponseDTO(
            OrderEntity order,
            List<OrderProductEntity> orderProduct,
            PaymentEntity payment
    ) {
        this.orderId = order.getId();
        this.createdAt = UUIDService.parseTimestampUUIDV7(this.orderId);
        this.items = orderProduct.stream().map(OrderProductResponseDTO::new).toList();
        this.result = order.getStatus();
        if(payment != null){
            this.payment = new PaymentInfoDTO(payment);
        }
    }

    @Getter
    @Setter
    public static class OrderProductResponseDTO {
        @JsonProperty("id")
        private UUID productId;
        @JsonProperty("name")
        private String productName;
        @JsonProperty("image")
        private String productImage;
        @JsonProperty("price")
        private Double productPrice;
        @JsonProperty("quality")
        private Integer productQuality;

        public OrderProductResponseDTO(OrderProductEntity orderProduct) {
            this.productPrice = orderProduct.getPrice();
            this.productQuality = orderProduct.getQuantity();
            this.productName = orderProduct.getProductName();
            this.productImage = orderProduct.getProductImage();

            ProductEntity productEntity = orderProduct.getProduct();
            if(productEntity != null){
                this.productId = productEntity.getId();
            }
        }
    }
}
