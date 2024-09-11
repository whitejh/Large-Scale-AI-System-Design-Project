package com.team11.product_service.domain.model;

import com.team11.product_service.presentation.request.ProductReqDto;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="product_id")
    private UUID productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="company_id")
    private UUID companyId;

    @Column(name="hub_id")
    private UUID hubId;

    @Column(name="stock")
    private int stock;


}
