package dev.mottolab.storeapi.dto.request.order;

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
        @NotEmpty
        private String payMethod;
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
