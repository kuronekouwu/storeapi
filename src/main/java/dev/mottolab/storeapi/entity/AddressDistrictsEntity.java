package dev.mottolab.storeapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "address_districts")
@Getter
@Setter
public class AddressDistrictsEntity {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name_th")
    private String nameTH;
    @Column(name = "name_en")
    private String nameEN;
    @ManyToOne
    @JoinColumn(columnDefinition = "province_id", referencedColumnName = "id")
    private AddressProvincesEntity province;
    @OneToMany(mappedBy = "district")
    private List<AddressSubDistrictsEntity> subDistricts;
}
