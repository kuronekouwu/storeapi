package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.enitity.UserInfoEnitity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfoEnitity, Integer> {
}
