package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class PaymentEntity {
    @Id
    private UUID id;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Column(unique = true, nullable = false)
    private String transactionId;
    @Column(nullable = false)
    private Double amount;
    @Column
    private String qrCode;
    @Column
    private String ref1;
    @Column
    private String ref2;
    @Column
    private String ref3;
    @Column(columnDefinition = "TEXT")
    private String metadata;
    @OneToOne(mappedBy = "payment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private OrderEntity order;

    @PrePersist
    protected void onCreate() {
        this.id = UUIDService.generateUUIDV7();
    }
}
