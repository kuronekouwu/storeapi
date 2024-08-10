package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private Double total = 0.0;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order")
    private List<OrderProductEntity> orderProducts;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserInfoEntity user;
    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private PaymentEntity payment;
    @PrePersist
    protected void onCreate() {
        this.id = UUIDService.generateUUIDV7();
    }
}
