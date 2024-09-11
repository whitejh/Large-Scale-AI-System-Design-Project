package com.team11.company_service.presentation.controller;

import com.team11.company_service.application.dto.CompanyRespDto;
import com.team11.company_service.application.service.CompanyService;
import com.team11.company_service.presentation.request.CompanyReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name="Company", description="Company API")
@RestController
@RequestMapping("/companies")
@Slf4j
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary="업체 생성", description="새 업체를 생성합니다.")
    @PostMapping
    public ResponseEntity<CompanyRespDto> createCompany(CompanyReqDto companyReqDto) {
        return ResponseEntity.ok(companyService.createCompany(companyReqDto));
    }

    @Operation(summary="업체 수정", description="업체를 수정합니다.")
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyRespDto> updateCompany(CompanyReqDto companyReqDto, @PathVariable UUID companyId) {
        return ResponseEntity.ok(companyService.updateCompany(companyReqDto, companyId));
    }

    @Operation(summary="업체 삭제", description="업체를 삭제합니다.")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<CompanyRespDto> deleteCompany(@PathVariable UUID companyId, @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(companyService.deleteCompany(companyId, userName));
    }

    @Operation(summary="허브 소속 업체 조회", description="해당 허브에 속한 업체들을 전체 조회합니다.")
    @GetMapping("/{hubId}")
    public ResponseEntity<List<CompanyRespDto>> getCompanies(@PathVariable UUID hubId) {
        return ResponseEntity.ok(companyService.getCompanies(hubId));
    }

    @Operation(summary="업체 상세 조회", description="특정 업체를 상세 조회합니다.")
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyRespDto> getCompanyDetails(@PathVariable UUID companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetails(companyId));
    }

    @Operation(summary="업체 ID 확인", description="업체의 존재 여부를 확인합니다.")
    @GetMapping("/checkCompany/{companyId}")
    public boolean checkCompany(@PathVariable UUID companyId) {
        return companyService.checkCompany(companyId);
    }

    @Operation(summary="업체의 소속 허브 확인", description="업체가 속한 허브를 반환합니다.")
    @GetMapping("/getHub/{companyId}")
    public UUID getHubIdByCompanyId(@PathVariable UUID companyId){
        return companyService.getHubIdByCompanyId(companyId);
    }

    @Operation(summary="업체 주소 확인", description="배송지로 사용될 업체의 주소를 반환합니다.")
    @GetMapping("/getCompanyAddy/{companyId}")
    public String getCompanyAddy(@PathVariable UUID companyId) {
        return companyService.getCompanyAddy(companyId);
    }




}
