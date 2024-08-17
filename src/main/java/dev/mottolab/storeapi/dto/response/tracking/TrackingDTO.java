package dev.mottolab.storeapi.dto.response.tracking;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.ShippingEntity;
import dev.mottolab.storeapi.entity.ShippingTrackerEntity;
import dev.mottolab.storeapi.entity.shipping.ShippingTrackerType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrackingDTO {
    @JsonProperty("tracking_id")
    private String trackingId;
    @JsonProperty("method")
    private ShippingTrackerType trackingMethod;
    private List<TrackingItemsDTO> items;

    public TrackingDTO(ShippingEntity entity, List<ShippingTrackerEntity> items) {
        this.trackingId = entity.getTrackingNumber();
        this.trackingMethod = entity.getShippingMethod();
        this.items = items.stream().map(TrackingItemsDTO::new).toList();
    }

    @Getter
    @Setter
    public static class TrackingItemsDTO {
        private String status;
        private String description;
        private String location;
        @JsonProperty("receiver_name")
        private String receiverName;

        public TrackingItemsDTO(ShippingTrackerEntity item) {
            this.status = item.getStatus();
            this.description = item.getDescription();
            this.location = item.getLocation();
            this.receiverName = item.getReceiveName();
        }
    }
}
