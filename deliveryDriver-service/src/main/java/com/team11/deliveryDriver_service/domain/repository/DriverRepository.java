package com.team11.deliveryDriver_service.domain.repository;

import com.team11.deliveryDriver_service.domain.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByUserIdAndDeletedIsFalse(Long userId);
    Optional<Page<Driver>> findAllByHubIdAndDeletedIsFalseOrderByCreatedAtDesc(UUID hubId, Pageable pageable);

}
