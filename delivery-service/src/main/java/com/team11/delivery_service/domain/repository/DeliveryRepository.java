package com.team11.delivery_service.domain.repository;

import com.team11.delivery_service.domain.model.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    Optional<Delivery> findByDeliveryIdAndDeletedIsFalse(UUID deliveryId);

    @Query("SELECT d from Delivery d WHERE (d.originHubId=:hubId OR d.destinationHubId=:hubId) AND d.deleted = false")
    Optional<Page<Delivery>> findAllByHubIdAndDeletedIsFalseOrderByCreatedAtDesc(UUID hubId, Pageable pageable);

    Optional<Page<Delivery>> findAllByDeliveryAddressAndDeletedIsFalseOrderByCreatedAtDesc(String deliveryAddress, Pageable pageable);
}
