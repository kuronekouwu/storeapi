package dev.mottolab.storeapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "address_provices")
@Getter
@Setter
public class AddressProvincesEntity {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name_th")
    private String nameTH;
    @Column(name = "name_en")
    private String nameEN;
    @OneToMany(mappedBy = "province")
    private List<AddressDistrictsEntity> district;
}
