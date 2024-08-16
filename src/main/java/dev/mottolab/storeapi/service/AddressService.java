package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.AddressDistrictsEntity;
import dev.mottolab.storeapi.entity.AddressProvincesEntity;
import dev.mottolab.storeapi.entity.AddressSubDistrictsEntity;
import dev.mottolab.storeapi.repository.AddressDistrictRepository;
import dev.mottolab.storeapi.repository.AddressProvinceRepository;
import dev.mottolab.storeapi.repository.AddressSubDistrictRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressProvinceRepository provinceRepository;
    private final AddressDistrictRepository districtRepository;
    private final AddressSubDistrictRepository subDistrictRepository;

    public AddressService(
            AddressProvinceRepository provinceRepository,
            AddressDistrictRepository districtRepository,
            AddressSubDistrictRepository subDistrictRepository
    ){
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.subDistrictRepository = subDistrictRepository;
    }

    public List<AddressProvincesEntity> getAllProvinces() {
        return provinceRepository.findAll(Sort.by(Sort.Direction.ASC, "nameTH"));
    }

    public List<AddressDistrictsEntity> getAllDistrictsByProvinceId(Integer provinceId) {
        return this.districtRepository.findAllByProvinceId(provinceId, Sort.by(Sort.Direction.ASC, "nameTH"));
    }

    public List<AddressSubDistrictsEntity> getAllSubDistrictsByDistrictId(Integer districtId) {
        return this.subDistrictRepository.findAllByDistrictId(districtId, Sort.by(Sort.Direction.ASC, "nameTH"));
    }

    public Optional<AddressSubDistrictsEntity> getSubDistrictById(Integer addressId) {
        return this.subDistrictRepository.findById(addressId);
    }
}
