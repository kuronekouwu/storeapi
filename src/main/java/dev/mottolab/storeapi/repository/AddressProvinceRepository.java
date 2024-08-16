package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.AddressProvincesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressProvinceRepository extends JpaRepository<AddressProvincesEntity, Integer> {}
