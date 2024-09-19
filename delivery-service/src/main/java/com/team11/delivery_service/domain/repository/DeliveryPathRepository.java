package com.team11.delivery_service.domain.repository;

import com.team11.delivery_service.domain.model.DeliveryPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, UUID> {
}
