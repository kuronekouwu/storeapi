package dev.mottolab.storeapi.entity;

import dev.mottolab.storeapi.entity.shipping.ShippingTrackerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "shipping")
@Getter
@Setter
public class ShippingEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true, name = "tracking_no")
    private String trackingNumber;
    @Column
    private String fullName;
    @Column
    private String line1;
    @Column
    private String line2;
    @Column
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private ShippingTrackerType shippingMethod;
    @ManyToOne
    @JoinColumn(columnDefinition = "address_id", referencedColumnName = "id")
    private AddressSubDistrictsEntity address;
    @OneToOne(mappedBy = "shipping")
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(columnDefinition = "shipping_rate_id", referencedColumnName = "id")
    private ShippingRateEntity shippingRate;
    @OneToMany(mappedBy = "shipping")
    private List<ShippingTrackerEntity> trackers;
}
