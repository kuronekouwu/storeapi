package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.response.address.DistrictDTO;
import dev.mottolab.storeapi.dto.response.address.ProvinceDTO;
import dev.mottolab.storeapi.dto.response.address.SubDistrictDTO;
import dev.mottolab.storeapi.service.AddressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/getProvinces")
    public List<ProvinceDTO> getAllProvince(){
        return this.addressService.getAllProvinces()
                .stream()
                .map(ProvinceDTO::new)
                .toList();
    }

    @GetMapping("/getDistricts")
    public List<DistrictDTO> getDistrictByProvinceId(
            @RequestParam("id") int provinceId
    ){
        return this.addressService.getAllDistrictsByProvinceId(provinceId)
                .stream()
                .map(DistrictDTO::new)
                .toList();
    }

    @GetMapping("/getSubDistricts")
    public List<SubDistrictDTO> getSubDistrictByDistrictId(
            @RequestParam("id") int districtId
    ){
        return this.addressService.getAllSubDistrictsByDistrictId(districtId)
                .stream()
                .map(SubDistrictDTO::new)
                .toList();
    }
}
