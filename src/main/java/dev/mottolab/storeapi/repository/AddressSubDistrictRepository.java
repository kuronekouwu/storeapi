package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.AddressSubDistrictsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressSubDistrictRepository extends JpaRepository<AddressSubDistrictsEntity, Integer> {
    public List<AddressSubDistrictsEntity> findAllByDistrictId(Integer addressId, Sort sort);
}
