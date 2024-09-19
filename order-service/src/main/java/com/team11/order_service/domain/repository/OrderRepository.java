package com.team11.order_service.domain.repository;

import com.team11.order_service.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByOrderIdAndDeletedIsFalse(UUID orderId);

    @Query("SELECT o FROM Order o WHERE o.supplyCompanyId=:companyId OR o.receiveCompanyId=:companyId AND o.deleted = False order by o.createdAt desc")
    Optional<Page<Order>> findAllBySupplyCompanyIdOrReceiveCompanyIdAndDeletedIsFalseOrderByCreatedAtDesc(UUID companyId, Pageable pageable);
}
