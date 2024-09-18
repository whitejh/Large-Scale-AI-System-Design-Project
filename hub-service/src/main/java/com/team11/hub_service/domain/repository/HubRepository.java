package com.team11.hub_service.domain.repository;

import com.team11.hub_service.domain.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubRepository extends JpaRepository<Hub, UUID> {

    //Optional<Hub> findByHubId(UUID hubId);
    Optional<Hub> findByHubIdAndDeleteFalse(UUID hubId);
    Optional<Hub> findByNameContainingAndDeleteFalse(String hubName); // 검색
    Optional<Page<Hub>> findByHubIdAndDeleteFalseOrderByCreatedAtDesc(UUID hubId, Pageable pageable);

}
