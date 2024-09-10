package com.team11.product_service.application.service;

import com.team11.product_service.application.dto.ProductRespDto;
import com.team11.product_service.domain.model.Product;
import com.team11.product_service.domain.repository.ProductRepository;
import com.team11.product_service.presentation.request.ProductReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 추가
    public ProductRespDto createProduct(ProductReqDto req) {
        Product product = ProductReqDto.toProduct(req);

        return ProductRespDto.from(productRepository.save(product));
    }

    // 상품 수정
    public ProductRespDto updateProduct(ProductReqDto req, UUID productId) {
        UUID companyId = req.getCompanyId();

        Product product = productRepository.findByProductIdAndDeletedIsFalse(productId).orElseThrow(
                ()-> new IllegalArgumentException("수정하려는 상품이 존재하지 않습니다.")
        );

        product.setProductName(req.getProductName());
        product.setStock(req.getStock());

        productRepository.save(product);

        return ProductRespDto.from(productRepository.save(product));
    }

    // 상품 삭제
    public ProductRespDto deleteProduct(UUID productId, String userName) {
        Product product = productRepository.findByProductIdAndDeletedIsFalse(productId).orElseThrow(
                ()-> new IllegalArgumentException("삭제하려는 상품이 존재하지 않습니다.")
        );

        product.setDeleted(LocalDateTime.now(), userName);
        return ProductRespDto.from(productRepository.save(product));
    }

    // 상품 전체 조회
    public List<ProductRespDto> getAllProducts(UUID companyId) {
        List<Product> productList = productRepository.findAllByCompanyIdAndDeletedIsFalse(companyId).orElseThrow(
                ()-> new IllegalArgumentException("해당 업체의 상품이 존재하지 않습니다.")
        );

        return productList.stream().map(ProductRespDto::from).collect(Collectors.toList());
    }

    // 상품 상세 조회
    public ProductRespDto searchProduct(UUID productId) {
        Product product = productRepository.findByProductIdAndDeletedIsFalse(productId).orElseThrow(
                ()-> new IllegalArgumentException("검색하려는 상품이 존재하지 않습니다.")
        );

        return ProductRespDto.from(product);
    }

    // 상품 재고 확인 (feignClient)
    public int getStock(UUID productId) {
        int stock = productRepository.findStockByProductId(productId);
        return stock;
    }

    // 상품 재고 업데이트 (feignClient)
    public void updateStock(UUID productId, int stock) {
        Product product = productRepository.findByProductIdAndDeletedIsFalse(productId).orElseThrow(
                ()-> new IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
        );

        product.setStock(stock);
        productRepository.save(product);
    }

}
