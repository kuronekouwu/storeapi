package dev.mottolab.storeapi.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateOrderByBasketIdDTO{
        @NotNull
        @Min(1)
        @JsonProperty("address_id")
        private int addressId;
        @NotNull
        @Min(1)
        @JsonProperty("shipping_id")
        private int shippingId;
        @NotNull
        @JsonProperty("pay_method")
        private PaymentMethod paymentMethod;
        @NotNull
        @NotEmpty
        private final List<BasketList> items = new ArrayList<>();

        @Getter
        @Setter
        public static class BasketList {
                @Min(1)
                @NotNull
                private Integer id;
                @Min(1)
                @NotNull
                private Integer unit;
        }
}
