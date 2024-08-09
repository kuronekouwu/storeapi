package dev.mottolab.storeapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "order_products")
@Getter
@Setter
public class OrderProductEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;
    // Metadata for if product has been deleted
    @Column(nullable = false)
    private String productName;
    @Column(nullable = true)
    private String productImage;
    @Column(nullable = false)
    private Integer quantity = 1;
    @Column(nullable = false)
    private Double price;
}
