package dev.mottolab.storeapi.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum UserRole {
    USER(Collections.emptySet()),
    ADMIN(Set.of(
            UserPermission.PRODUCT_CREATE,
            UserPermission.PRODUCT_DELETE,
            UserPermission.PRODUCT_UPDATE
    ));

    @Getter
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
