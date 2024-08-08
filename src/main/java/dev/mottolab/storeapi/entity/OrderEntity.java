package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private Double total;
    @OneToMany(mappedBy = "order")
    private List<OrderProductEntity> orderProducts;
    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private PaymentEntity payment;
    @PrePersist
    protected void onCreate() {
        this.id = UUIDService.generateUUIDV7();
    }
}
