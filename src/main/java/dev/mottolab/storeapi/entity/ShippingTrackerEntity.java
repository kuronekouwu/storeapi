package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.entity.shipping.ShippingTrackerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "shipping_tracker")
@Getter
@Setter
public class ShippingTrackerEntity {
    @Id
    @GeneratedValue
    private int id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "delivered_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredAt;
    @Column
    private String status;
    @Column
    private String description;
    @Column(name = "receive_name")
    private String receiveName;
    @Column
    private String location;
    @ManyToOne
    @JoinColumn(columnDefinition = "shipping_id", referencedColumnName = "id")
    private ShippingEntity shipping;
}
