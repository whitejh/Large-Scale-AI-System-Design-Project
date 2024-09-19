package com.team11.delivery_service.infrastructure.feign;

import com.team11.delivery_service.application.dto.PathResultsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name="hub-service")
public interface HubPathsFeignClient {
    @GetMapping("/api/hubPaths/getHubPaths/{startHubId}/{endHubId}")
    public List<PathResultsDto> getHubPaths(@PathVariable("startHubId") UUID startHubId, @PathVariable("endHubId") UUID endHubId);
}
