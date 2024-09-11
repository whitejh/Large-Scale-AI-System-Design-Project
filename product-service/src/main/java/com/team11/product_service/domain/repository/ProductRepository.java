package com.team11.product_service.domain.repository;

import com.team11.product_service.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByProductIdAndDeletedIsFalse(UUID productId);
    Optional<List<Product>> findAllByCompanyIdAndDeletedIsFalse(UUID companyId);
}
