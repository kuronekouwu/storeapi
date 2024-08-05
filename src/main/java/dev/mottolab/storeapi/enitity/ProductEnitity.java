package dev.mottolab.storeapi.enitity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEnitity {
    @Id
    private UUID id;
    @Column(unique = true, nullable = false, length = 64)
    private String slug;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String image;
    @Column(nullable = false)
    private Double price = 0.0;
}
