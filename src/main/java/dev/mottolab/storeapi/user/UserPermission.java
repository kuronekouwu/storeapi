package dev.mottolab.storeapi.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserPermission {
    // Product
    PRODUCT_CREATE("product:create"),
    PRODUCT_UPDATE("product:update"),
    PRODUCT_DELETE("product:delete");

    @Getter
    private final String permission;
}
