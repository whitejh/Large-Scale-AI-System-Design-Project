package com.team11.order_service.domain.repository;

import com.team11.order_service.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByOrderIdAndDeletedIsFalse(UUID orderId);
    Optional<List<Order>> findAllByUserNameAndDeletedIsFalse(String username);
}
