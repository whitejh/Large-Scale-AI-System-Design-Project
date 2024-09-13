package com.team11.company_service.domain.repository;

import com.team11.company_service.domain.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCompanyIdAndDeletedIsFalse(UUID companyId);
    Optional<Page<Company>> findAllByHubIdAndDeletedIsFalseOrderByCreatedAtDesc(UUID hubId, Pageable pageable);
    boolean existsByCompanyIdAndDeletedIsFalse(UUID companyId);
}