package dev.mottolab.storeapi.user;

import dev.mottolab.storeapi.enitity.IdentifyEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class UserInfoDetail implements UserDetails {
    @lombok.Getter
    private UUID userId;
    private String email;
    private String password;
    @lombok.Getter
    private String displayName;
    @lombok.Getter
    private UserRole roles;

    public UserInfoDetail(IdentifyEntity iden) {
        this.userId = iden.getId();
        this.email = iden.getEmail();
        this.password = iden.getPassword();
        this.displayName = iden.getUser().getDisplayName();
        this.roles = iden.getUser().getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.getAuthorities();

    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
