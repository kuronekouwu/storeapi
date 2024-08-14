package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private UUID id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(nullable = false)
    private Boolean isDeleted = false;
    @Column(unique = true, nullable = false, length = 255)
    private String slug;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String image;
    @Column(nullable = false)
    private Double price = 0.0;
    @Column(nullable = false)
    private Integer stock = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity category;
    @OneToMany(mappedBy = "product")
    private List<BasketEntity> baskets;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderProductEntity> orderProduct;

    @PrePersist
    protected void onCreate() {
        this.id = UUIDService.generateUUIDV7();
    }
}
