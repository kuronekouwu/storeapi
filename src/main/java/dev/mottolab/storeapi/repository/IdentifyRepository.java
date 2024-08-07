package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.entity.IdentifyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentifyRepository extends JpaRepository<IdentifyEntity, Integer> {
    Optional<IdentifyEntity> findByEmail(String username);
}
