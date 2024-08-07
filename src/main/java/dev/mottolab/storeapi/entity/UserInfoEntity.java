package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.user.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @OneToOne(mappedBy = "user")
    private IdentifyEntity identity;
}
