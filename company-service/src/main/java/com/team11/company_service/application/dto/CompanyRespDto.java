package com.team11.company_service.application.dto;

import com.team11.company_service.domain.model.Company;
import com.team11.company_service.domain.model.CompanyTypeEnum;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRespDto {
    private UUID companyId;
    private String companyName;
    private CompanyTypeEnum type;
    private UUID hubId;
    private String companyAddress;

    public static CompanyRespDto from(Company company) {
        return CompanyRespDto.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .type(company.getType())
                .hubId(company.getHubId())
                .companyAddress(company.getCompanyAddress())
                .build();
    }
}
