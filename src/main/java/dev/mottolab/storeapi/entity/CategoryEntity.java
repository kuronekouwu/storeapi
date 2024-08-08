package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    private UUID id;
    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String slug;
    @Column
    private String name;
    @Column(nullable = true)
    private String description;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<ProductEntity> products;
    @PrePersist
    protected void onCreate() {
        this.id = UUIDService.generateUUIDV7();
    }
}
