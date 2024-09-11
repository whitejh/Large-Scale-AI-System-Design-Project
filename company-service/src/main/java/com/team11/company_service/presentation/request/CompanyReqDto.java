package com.team11.company_service.presentation.request;

import com.team11.company_service.domain.model.Company;
import com.team11.company_service.domain.model.CompanyTypeEnum;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyReqDto {
    private UUID companyId;
    private String companyName;
    private CompanyTypeEnum type;
    private UUID hubId;
    private String companyAddress;

    public static Company toCompany(CompanyReqDto reqDto){
        return Company.builder()
                .companyId(reqDto.getCompanyId())
                .companyName(reqDto.getCompanyName())
                .type(reqDto.getType())
                .hubId(reqDto.getHubId())
                .companyAddress(reqDto.getCompanyAddress())
                .build();
    }
}
