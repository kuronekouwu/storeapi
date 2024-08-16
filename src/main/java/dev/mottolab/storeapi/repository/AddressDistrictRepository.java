package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.AddressDistrictsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressDistrictRepository extends JpaRepository<AddressDistrictsEntity, Integer> {
    List<AddressDistrictsEntity> findAllByProvinceId(int provinceId, Sort sort);
}
