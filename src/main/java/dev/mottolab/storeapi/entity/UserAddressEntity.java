package dev.mottolab.storeapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "user_address")
@Getter
@Setter
public class UserAddressEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column
    private String fullName;
    @Column
    private String line1;
    @Column
    private String line2;
    @Column
    private String phoneNumber;

    // Relation
    @ManyToOne
    @JoinColumn(columnDefinition = "address_id", referencedColumnName = "id")
    private AddressSubDistrictsEntity address;
    @ManyToOne
    @JoinColumn(columnDefinition = "user_id")
    private UserInfoEntity user;
}
