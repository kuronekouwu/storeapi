package dev.mottolab.storeapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shipping")
@Getter
@Setter
public class ShippingEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String fullName;
    @Column
    private String line1;
    @Column
    private String line2;
    @Column
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(columnDefinition = "address_id", referencedColumnName = "id")
    private AddressSubDistrictsEntity address;
    @OneToOne(mappedBy = "shipping")
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(columnDefinition = "shipping_rate_id", referencedColumnName = "id")
    private ShippingRateEntity shippingRate;
}
