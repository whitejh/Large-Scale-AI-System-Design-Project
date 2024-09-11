package com.team11.company_service.presentation.request;

import com.team11.company_service.domain.model.Company;
import com.team11.company_service.domain.model.CompanyTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyReqDto {
    @NotBlank(message="업체 이름을 꼭 입력해주세요.")
    private String companyName;
    private CompanyTypeEnum type;
    private UUID hubId;
    @NotBlank(message="주소를 꼭 입력해주세요.")
    private String companyAddress;

    public static Company toCompany(CompanyReqDto reqDto){
        return Company.builder()
                .companyName(reqDto.getCompanyName())
                .type(reqDto.getType())
                .hubId(reqDto.getHubId())
                .companyAddress(reqDto.getCompanyAddress())
                .build();
    }
}
