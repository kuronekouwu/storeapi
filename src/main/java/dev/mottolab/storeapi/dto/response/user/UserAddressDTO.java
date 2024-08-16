package dev.mottolab.storeapi.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.AddressSubDistrictsEntity;
import dev.mottolab.storeapi.entity.UserAddressEntity;
import lombok.Getter;

@Getter
public class UserAddressDTO {
    private final int id;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("address_line_1")
    private String addressLine1;
    @JsonProperty("address_line_2")
    private String addressLine2;
    @JsonProperty("sub_district")
    private String subDistrict = null;
    @JsonProperty("district")
    private String district = null;
    @JsonProperty("province")
    private String province = null;
    @JsonProperty("zip_code")
    private Integer zipCode = null;


    public UserAddressDTO(UserAddressEntity entity) {
        this.id = entity.getId();
        this.fullName = entity.getFullName();
        this.addressLine1 = entity.getLine1();
        this.addressLine2 = entity.getLine2();

        AddressSubDistrictsEntity addressInfo = entity.getAddress();
        if (addressInfo != null) {
            this.subDistrict = addressInfo.getNameTH();
            this.district = addressInfo.getDistrict().getNameTH();
            this.province = addressInfo.getDistrict().getProvince().getNameTH();
            this.zipCode = addressInfo.getZipCode();
        }
    }
}
