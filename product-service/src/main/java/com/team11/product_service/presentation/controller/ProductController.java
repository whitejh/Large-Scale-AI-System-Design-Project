package com.team11.product_service.presentation.controller;

import com.team11.product_service.application.dto.ProductRespDto;
import com.team11.product_service.application.service.ProductService;
import com.team11.product_service.presentation.request.ProductReqDto;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary="상품 추가", description="업체에 상품을 추가합니다.")
    @PostMapping
    public ResponseEntity<ProductRespDto> createProduct(@Validated @RequestBody ProductReqDto requestDto){
        return ResponseEntity.ok(productService.createProduct(requestDto));
    }

    @Operation(summary="상품 수정", description="상품의 정보를 수정합니다.")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductRespDto> updateProduct(@Validated @RequestBody ProductReqDto requestDto, @PathVariable UUID productId){
        return ResponseEntity.ok(productService.updateProduct(requestDto, productId));
    }

    @Operation(summary="상품 삭제", description="특정 상품을 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductRespDto> deleteProduct(@PathVariable UUID productId, @RequestHeader(name="X-User-Name", required = false) String userName){
        return ResponseEntity.ok(productService.deleteProduct(productId, userName));
    }

    @Operation(summary="상품 전제 조회", description="특정 업체의 전체 상품을 조회합니다.")
    @GetMapping("/search/{companyId}")
    public ResponseEntity<List<ProductRespDto>> getProducts(@PathVariable UUID companyId){
        return ResponseEntity.ok(productService.getAllProducts(companyId));
    }

    @Operation(summary="상품 상세 조회", description="특정 상품을 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductRespDto> searchProduct(@PathVariable UUID productId){
        return ResponseEntity.ok(productService.searchProduct(productId));
    }

    @Operation(summary="상품 재고 확인", description="특정 상품의 재고를 반환합니다.")
    @GetMapping("/stock/{productId}")
    public int getStockByProductId(@PathVariable UUID productId){
        return productService.getStock(productId);
    }

    @Operation(summary="상품 재고 갱신", description="특정 상품의 재고를 갱신합니다.")
    @PutMapping("/stock/{productId}")
    public void updateStockByProductId(@PathVariable UUID productId, int stock){
        productService.updateStock(productId, stock);
    }
}
