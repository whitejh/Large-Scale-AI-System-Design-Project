package com.team11.product_service.domain.repository;

import com.team11.product_service.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByProductIdAndDeletedIsFalse(UUID productId);
    Optional<Page<Product>> findByCompanyIdAndDeletedIsFalseOrderByCreatedAtDesc(UUID companyId, Pageable pageable);
}
