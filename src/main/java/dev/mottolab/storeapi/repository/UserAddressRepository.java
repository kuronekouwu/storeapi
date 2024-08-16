package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.UserAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Integer> {
    List<UserAddressEntity> findAllByUserId(Integer userId);
    Optional<UserAddressEntity> findByUserIdAndId(Integer userId, Integer Id);
}
