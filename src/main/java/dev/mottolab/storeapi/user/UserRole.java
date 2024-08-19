package dev.mottolab.storeapi.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER(Collections.emptySet()),
    ADMIN(Set.of(
            // Product
            UserPermission.PRODUCT_CREATE,
            UserPermission.PRODUCT_DELETE,
            UserPermission.PRODUCT_UPDATE,
            // Category
            UserPermission.CATEGORY_CREATE,
            UserPermission.CATEGORY_UPDATE,
            UserPermission.CATEGORY_DELETE,
            // Order
            UserPermission.ORDER_TRACKING_CREATE,
            UserPermission.ORDER_UPDATE
    ));

    private final Set<UserPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(perms -> new SimpleGrantedAuthority(perms.name()))
                .collect(Collectors.toList());


        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));

        return authorities;
    }
}
