package dev.mottolab.storeapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "address_sub_districts")
@Getter
@Setter
public class AddressSubDistrictsEntity {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name_th")
    private String nameTH;
    @Column(name = "name_en")
    private String nameEN;
    @Column(name = "zip_code")
    private int zipCode;
    @ManyToOne
    @JoinColumn(columnDefinition = "district_id", referencedColumnName = "id")
    private AddressDistrictsEntity district;
    @OneToMany(mappedBy = "address")
    private List<UserAddressEntity> userAddress;
    @OneToMany(mappedBy = "address")
    private List<ShippingEntity> shippingAddress;
}
