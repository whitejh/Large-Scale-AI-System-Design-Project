package com.team11.delivery_service.domain.repository;

import com.team11.delivery_service.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    Optional<Delivery> findByDeliveryIdAndDeletedIsFalse(UUID deliveryId);
}
