package dev.mottolab.storeapi.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPermission {
    // Product
    PRODUCT_CREATE("product:create"),
    PRODUCT_UPDATE("product:update"),
    PRODUCT_DELETE("product:delete"),
    // Category
    CATEGORY_CREATE("category:create"),
    CATEGORY_UPDATE("category:update"),
    CATEGORY_DELETE("category:delete"),
    // Order
    ORDER_TRACKING_CREATE("order:tracking_create"),
    ORDER_UPDATE("order:update");

    private final String permission;
}
