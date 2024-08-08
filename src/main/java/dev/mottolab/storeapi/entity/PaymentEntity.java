package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.entity.payment.PaymentMethod;
import dev.mottolab.storeapi.entity.payment.PaymentStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    private UUID id;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Column(unique = true)
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus result;
    @Column(nullable = false)
    private Double amount;
    @OneToOne(mappedBy = "payment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private OrderEntity order;
}
