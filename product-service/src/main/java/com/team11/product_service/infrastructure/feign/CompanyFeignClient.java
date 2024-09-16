package com.team11.product_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="company-service")
public interface CompanyFeignClient {

    @GetMapping("api/companies/checkCompany/{companyId}")
    public boolean checkCompany(@PathVariable UUID companyId);
}
