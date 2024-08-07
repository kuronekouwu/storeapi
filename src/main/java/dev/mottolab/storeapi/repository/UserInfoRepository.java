package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.enitity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {
}
