package dev.mottolab.storeapi.repository;

import dev.mottolab.storeapi.enitity.IdentifyEnitity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentifyRepository extends JpaRepository<IdentifyEnitity, Integer> {
    Optional<IdentifyEnitity> findByEmail(String username);
}
