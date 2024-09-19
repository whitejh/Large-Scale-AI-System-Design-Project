package com.team11.delivery_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="company-service")
public interface CompanyFeignClient {
    @GetMapping("/api/companies/checkHub/{companyId}")
    public UUID getHubIdByCompanyId(@PathVariable(name="companyId") UUID companyId);

    @GetMapping("/api/companies/getCompanyAddy/{companyId}")
    public String getCompanyAddy(@PathVariable(name="companyId") UUID companyId);

}
