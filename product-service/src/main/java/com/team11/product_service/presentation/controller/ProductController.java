package com.team11.product_service.presentation.controller;

import com.team11.product_service.application.dto.ProductRespDto;
import com.team11.product_service.application.service.ProductService;
import com.team11.product_service.presentation.request.ProductReqDto;
import io.swagger.v3.oas.annotations.*;
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

@Tag(name="Product", description="Product API")
@RestController
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 권한 -> MASTER, MANAGER(본인 허브 소속만), COMPANY(본인 업체 소속만)
    @Operation(summary="상품 추가", description="업체에 상품을 추가합니다.")
    @PostMapping
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY')")
    public ResponseEntity<ProductRespDto> createProduct(@Validated @RequestBody ProductReqDto requestDto,
                                                        @RequestHeader(name="X-User-Name") String userName,
                                                        @RequestHeader(name="X-User-Roles") String role){
        return ResponseEntity.ok(productService.createProduct(requestDto, userName, role));
    }

    // 권한 -> MASTER, MANAGER(본인 허브 소속만), COMPANY(본인 업체 소속만)
    @Operation(summary="상품 수정", description="상품의 정보를 수정합니다.")
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY')")
    public ResponseEntity<ProductRespDto> updateProduct(
                                                        @Validated @RequestBody ProductReqDto requestDto,
                                                        @PathVariable(name="productId") UUID productId,
                                                        @RequestHeader(name="X-User-Name") String userName,
                                                        @RequestHeader(name="X-User-Roles") String role){
        return ResponseEntity.ok(productService.updateProduct(requestDto, productId, userName, role));
    }

    // 권한 -> MASTER, MANAGER(본인 허브 소속만), COMPANY(본인 업체 소속만)
    @Operation(summary="상품 삭제", description="특정 상품을 삭제합니다.")
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<ProductRespDto> deleteProduct(
                                                        @PathVariable(name="productId") UUID productId,
                                                        @RequestHeader(name="X-User-Name", required = false) String userName,
                                                        @RequestHeader(name="X-User-Roles") String role){
        return ResponseEntity.ok(productService.deleteProduct(productId, userName, role));
    }

    // 권한 -> MASTER, MANAGER, COMPANY, DRIVER
    @Operation(summary="상품 전제 조회", description="특정 업체의 전체 상품을 조회합니다.")
    @GetMapping("/search/{companyId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY') or hasRole('DRIVER')")
    public ResponseEntity<List<ProductRespDto>> getProducts(
                                                            @PathVariable(name="companyId") UUID companyId,
                                                            @PageableDefault(size=10) Pageable pageable,
                                                            @RequestParam(name="size", required = false) Integer size
    ){
        // 서치 페이징 기준 확인
        if(size!=null && List.of(10, 30, 50).contains(size)){
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(productService.getAllProducts(companyId, pageable));
    }

    // 권한 -> MASTER, MANAGER, COMPANY, DRIVER
    @Operation(summary="상품 상세 조회", description="특정 상품을 조회합니다.")
    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY') or hasRole('DRIVER')")
    public ResponseEntity<ProductRespDto> searchProduct(@PathVariable(name="productId") UUID productId){
        return ResponseEntity.ok(productService.searchProduct(productId));
    }

    // FeignClient

    @Operation(summary="상품 재고 확인", description="특정 상품의 재고를 반환합니다.")
    @GetMapping("/stock/{productId}")
    public int getStockByProductId(@PathVariable(name="productId") UUID productId){
        return productService.getStock(productId);
    }

    @Operation(summary="상품 재고 갱신", description="특정 상품의 재고를 갱신합니다.")
    @PutMapping("/stock/{productId}")
    public void updateStockByProductId(
                                        @PathVariable(name="productId") UUID productId,
                                       @RequestParam(name="stock") int stock){
        productService.updateStock(productId, stock);
    }
}
