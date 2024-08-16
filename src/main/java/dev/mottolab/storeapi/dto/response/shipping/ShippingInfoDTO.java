package dev.mottolab.storeapi.dto.response.shipping;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.entity.ShippingEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingInfoDTO {
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
    @JsonProperty("shipping_rate")
    private ShippingRateDTO shippingRateDTO = null;

    public ShippingInfoDTO(ShippingEntity entity) {
        this.fullName = entity.getFullName();
        this.addressLine1 = entity.getLine1();
        this.addressLine2 = entity.getLine2();
        this.subDistrict = entity.getAddress().getNameTH();
        this.district = entity.getAddress().getDistrict().getNameTH();
        this.province = entity.getAddress().getDistrict().getProvince().getNameTH();
        this.zipCode = entity.getAddress().getZipCode();
        if(entity.getShippingRate() != null){
            this.shippingRateDTO = new ShippingRateDTO(entity.getShippingRate());
        }
    }
}