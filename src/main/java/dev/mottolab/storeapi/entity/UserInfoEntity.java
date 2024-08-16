package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.user.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_info")
@Getter
@Setter
public class UserInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false, name = "display_name")
    private String displayName;
    @Enumerated(EnumType.STRING)
    private UserRole roles;
    @Column(name = "default_address_id")
    private Integer defaultAddressId;
    @OneToOne(mappedBy = "user")
    private IdentifyEntity identity;
    @OneToMany(mappedBy = "user")
    private List<BasketEntity> baskets = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<UserAddressEntity> address = new ArrayList<>();
}
