package com.team11.company_service.application.service;

import com.team11.company_service.application.dto.CompanyRespDto;
import com.team11.company_service.domain.model.Company;
import com.team11.company_service.domain.repository.CompanyRepository;
import com.team11.company_service.infrastructure.feign.HubFeignClient;
import com.team11.company_service.infrastructure.feign.UserFeignClient;
import com.team11.company_service.presentation.request.CompanyReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    private HubFeignClient hubFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;


    // 업체 추가
    @Transactional
    public CompanyRespDto createCompany(CompanyReqDto reqDto, String userName, String role) {
        //허브 아이디 확인
        UUID hubId = reqDto.getHubId();

        if(!hubFeignClient.isHubIdExist(hubId)){
            throw new IllegalArgumentException("허브가 존재하지 않습니다.");
        }

        // 권한이 MANAGER 일 때 본인이 속한 허브의 업체인지 확인
        if(role.equals("MANAGER")){
            UUID userHubId = userFeignClient.getUserHubId(userName);

            if(!hubId.equals(userHubId)){
                throw new IllegalArgumentException("해당 업체 생성에 권한이 없습니다.");
            }
        }

        Company company = CompanyReqDto.toCompany(reqDto);

        company.setCreatedBy(userName);

        return CompanyRespDto.from(companyRepository.save(company));
    }

    // 업체 수정
    @Transactional
    public CompanyRespDto updateCompany(CompanyReqDto reqDto, UUID companyId, String userName, String role) {
        UUID hubId = reqDto.getHubId();

        // 권한 확인
        if(role.equals("MANAGER") || role.equals("COMPANY")){
            UUID userHubId = userFeignClient.getUserHubId(userName);

            if(!hubId.equals(userHubId)){
                throw new IllegalArgumentException("해당 업체 수정에 권한이 없습니다.");
            }
        }

        // 업체 존재 여부 확인
        Company company = companyRepository.findByCompanyIdAndDeletedIsFalse(companyId).orElseThrow(
                ()-> new IllegalArgumentException("수정하려는 업체를 찾을 수 없습니다.")
        );

        company.setCompanyAddress(reqDto.getCompanyAddress());
        company.setCompanyName(reqDto.getCompanyName());

        company.setUpdatedBy(userName);

        return CompanyRespDto.from(companyRepository.save(company));
    }

    // 업체 삭제
    @Transactional
    public CompanyRespDto deleteCompany(UUID companyId, String userName, String role) {
        Company company = companyRepository.findByCompanyIdAndDeletedIsFalse(companyId).orElseThrow(
                ()-> new IllegalArgumentException("삭제하려는 업체가 없거나 이미 삭제되었습니다.")
        );

        UUID hubId = company.getHubId();

        // 권한이 MANAGER 일 때 본인이 속한 허브의 업체인지 확인
        if(role.equals("MANAGER")){
            UUID userHubId = userFeignClient.getUserHubId(userName);

            if(!hubId.equals(userHubId)){
                throw new IllegalArgumentException("해당 업체 삭제에 권한이 없습니다.");
            }
        }

        company.setDeleted(LocalDateTime.now(), userName);

        return CompanyRespDto.from(companyRepository.save(company));
    }

    // 허브 소속 업체 조회
    public List<CompanyRespDto> getCompanies(UUID hubId, Pageable pageable) {
        Page<Company> companyList = companyRepository.findAllByHubIdAndDeletedIsFalseOrderByCreatedAtDesc(hubId, pageable).orElseThrow(
                ()-> new IllegalArgumentException("해당 허브에 업체가 존재하지 않습니다.")
        );

        return companyList.stream().map(CompanyRespDto::from).collect(Collectors.toList());
    }

    // 업체 상세 조회
    public CompanyRespDto getCompanyDetails(UUID companyId) {
        Company company = companyRepository.findByCompanyIdAndDeletedIsFalse(companyId).orElseThrow(
                ()-> new IllegalArgumentException("해당 업체가 존재하지 않습니다.")
        );

        return CompanyRespDto.from(company);
    }

    // FeignClient

    // 업체 존재 여부 확인
    public boolean checkCompany (UUID companyId){
        return companyRepository.existsByCompanyIdAndDeletedIsFalse(companyId);
    }

    // 업체의 소속 허브 아이디 반환
    public UUID getHubIdByCompanyId(UUID companyId) {
        Company company= companyRepository.findByCompanyIdAndDeletedIsFalse(companyId).orElseThrow(
                ()-> new IllegalArgumentException("해당 업체를 찾을 수 없습니다.")
        );

        UUID hubId = company.getHubId();
        return hubId;
    }

    // 업체의 주소 반환
    public String getCompanyAddy (UUID companyId) {
        Company company = companyRepository.findByCompanyIdAndDeletedIsFalse(companyId).orElseThrow(
                ()-> new IllegalArgumentException("해당 업체의 주소를 찾을 수 없습니다.")
        );

        String companyAddy = company.getCompanyAddress();

        return companyAddy;
    }

}
