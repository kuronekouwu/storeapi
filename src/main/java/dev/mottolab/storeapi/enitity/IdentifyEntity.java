package dev.mottolab.storeapi.enitity;

import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "identify")
@Getter
@Setter
public class IdentifyEntity {
    @Id
    private UUID id;
    @Column(
            nullable = false,
            name = "email",
            length = 128,
            unique = true
    )
    private String email;
    @Column(
            nullable = false,
            name = "password"
    )
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserInfoEntity user;
    @PrePersist
    protected void onCreate() {
        this.id = UUIDService.generateUUIDV7();
    }
}
