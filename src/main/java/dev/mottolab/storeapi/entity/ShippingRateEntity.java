package dev.mottolab.storeapi.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "shipping_rate")
@Getter
public class ShippingRateEntity {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(name = "price")
    private Double price;
    @OneToMany(mappedBy = "shippingRate")
    private List<ShippingEntity> shipping;
}
