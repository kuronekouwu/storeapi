package dev.mottolab.storeapi.enitity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "catgories")
public class CategoryEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String slug;
    @Column
    private String name;
    @Column(nullable = true)
    private String description;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<ProductEntity> products;
}
