package com.team11.company_service.presentation.controller;

import com.team11.company_service.application.dto.CompanyRespDto;
import com.team11.company_service.application.service.CompanyService;
import com.team11.company_service.presentation.request.CompanyReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="Company", description="Company API")
@RestController
@RequestMapping("/companies")
@Slf4j
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 권한 -> MASTER, MANAGER(본인 허브 소속만)
    @Operation(summary="업체 생성", description="새 업체를 생성합니다.")
    @PostMapping
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER')")
    public ResponseEntity<CompanyRespDto> createCompany(@Validated @RequestBody CompanyReqDto companyReqDto,
                                                        @RequestHeader(name="X-User-Name") String userName,
                                                        @RequestHeader(name="X-User-Roles") String role) {
        return ResponseEntity.ok(companyService.createCompany(companyReqDto, userName, role));
    }

    // 권한 -> MASTER, MANAGER(본인 허브 소속만), COMPANY(본인 업체 소속만)
    @Operation(summary="업체 수정", description="업체를 수정합니다.")
    @PutMapping("/{companyId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY')")
    public ResponseEntity<CompanyRespDto> updateCompany(
                                                        @Validated @RequestBody CompanyReqDto companyReqDto,
                                                        @PathVariable(name="companyId") UUID companyId,
                                                        @RequestHeader(name="X-User-Name") String userName,
                                                        @RequestHeader(name="X-User-Roles") String role) {
        return ResponseEntity.ok(companyService.updateCompany(companyReqDto, companyId, userName, role));
    }

    // 권한 -> MASTER, MANAGER(본인 허브 소속만)
    @Operation(summary="업체 삭제", description="업체를 삭제합니다.")
    @DeleteMapping("/{companyId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER')")
    public ResponseEntity<CompanyRespDto> deleteCompany(
                                                        @PathVariable(name="companyId") UUID companyId,
                                                        @RequestHeader(name="X-User-Name") String userName,
                                                        @RequestHeader(name="X-User-Roles") String role) {
        return ResponseEntity.ok(companyService.deleteCompany(companyId, userName, role));
    }

    // 권한 -> MASTER, MANAGER, COMPANY, DRIVER
    @Operation(summary="허브 소속 업체 조회", description="해당 허브에 속한 업체들을 전체 조회합니다.")
    @GetMapping("/search/{hubId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY') or hasRole('DRIVER')")
    public ResponseEntity<List<CompanyRespDto>> getCompanies(
                                                            @PathVariable(name="hubId") UUID hubId,
                                                            @PageableDefault(size=10) Pageable pageable,
                                                            @RequestParam(name="size",required = false) Integer size
                                                             ) {
        // 서치 페이징 기준 확인
        if (size != null && List.of(10, 30, 50).contains(size)) {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(companyService.getCompanies(hubId, pageable));
    }

    // 권한 -> MASTER, MANAGER, COMPANY, DRIVER
    @Operation(summary="업체 상세 조회", description="특정 업체를 상세 조회합니다.")
    @GetMapping("/{companyId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY') or hasRole('DRIVER')")
    public ResponseEntity<CompanyRespDto> getCompanyDetails(@PathVariable(name="companyId") UUID companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetails(companyId));
    }

    // FeignClient

    @Operation(summary="업체 ID 확인", description="업체의 존재 여부를 확인합니다.")
    @GetMapping("/checkCompany/{companyId}")
    public boolean checkCompany(@PathVariable(name="companyId") UUID companyId) {
        return companyService.checkCompany(companyId);
    }

    @Operation(summary="업체의 소속 허브 확인", description="업체가 속한 허브를 반환합니다.")
    @GetMapping("/checkHub/{companyId}")
    public UUID getHubIdByCompanyId(@PathVariable(name="companyId") UUID companyId){
        return companyService.getHubIdByCompanyId(companyId);
    }

    @Operation(summary="업체 주소 확인", description="배송지로 사용될 업체의 주소를 반환합니다.")
    @GetMapping("/getCompanyAddy/{companyId}")
    public String getCompanyAddy(@PathVariable(name="companyId") UUID companyId) {
        return companyService.getCompanyAddy(companyId);
    }




}
