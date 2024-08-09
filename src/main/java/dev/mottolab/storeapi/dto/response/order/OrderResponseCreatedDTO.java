package dev.mottolab.storeapi.dto.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.service.utils.UUIDService;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderResponseCreatedDTO {
    @JsonProperty("order_id")
    private UUID orderId;
    @JsonProperty("created_at")
    private Long createdAt;

    public OrderResponseCreatedDTO(OrderEntity order) {
        this.orderId = order.getId();
        this.createdAt = UUIDService.parseTimestampUUIDV7(this.orderId.toString());
    }
}
