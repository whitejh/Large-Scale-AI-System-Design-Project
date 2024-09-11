package com.team11.company_service.domain.repository;

import com.team11.company_service.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCompanyIdAndDeletedIsFalse(UUID companyId);
    Optional<List<Company>> findAllByHubIdAndDeletedIsFalse(UUID hubId);
}
