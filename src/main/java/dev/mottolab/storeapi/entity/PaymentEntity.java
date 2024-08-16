package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.entity.order.OrderStatus;
import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import dev.mottolab.storeapi.entity.payment.PaymentStatus;
import dev.mottolab.storeapi.service.utils.UUIDService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class PaymentEntity {
    @Id
    private UUID id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column
    private Date paidAt;
    @Column
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
    @Column(unique = true)
    private String transactionId;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false, name = "acual_pay")
    private Double acualPay = 0.0;
    @Column
    private String qrCode;
    @Column
    private String ref1;
    @Column
    private String ref2;
    @Column
    private String ref3;
    @Column(nullable = true)
    private String ipAddress;
    @Column(columnDefinition = "TEXT")
    private String metadata;
    @OneToOne(mappedBy = "payment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private OrderEntity order;

    @PrePersist
    protected void onCreate() {
        this.id = UUIDService.generateUUIDV7();
    }
}
