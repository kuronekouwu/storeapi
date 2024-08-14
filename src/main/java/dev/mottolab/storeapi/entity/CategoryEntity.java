package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    private UUID id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(
            nullable = false,
            unique = true
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
