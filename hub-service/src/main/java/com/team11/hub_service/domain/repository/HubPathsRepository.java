package com.team11.hub_service.domain.repository;

import com.team11.hub_service.domain.model.Hub;
import com.team11.hub_service.domain.model.HubPaths;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubPathsRepository extends JpaRepository<HubPaths, UUID> {

 //   Page<HubPathResponse> searchHubPath(String keyword, Pageable pageable);

//    Optional<HubPaths> findByHubIdAndDeleteFalse(UUID hubId);
    Optional<HubPaths> findBypathContainingAndDeleteFalse(String path); // 검색
    Optional<Page<HubPaths>> findByHubPathsIdAndDeleteFalseOrderByCreatedAtDesc(UUID hubPathsId, Pageable pageable);
}
