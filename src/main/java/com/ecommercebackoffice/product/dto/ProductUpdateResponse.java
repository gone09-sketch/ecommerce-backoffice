package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductUpdateResponse {

    private final Long id;
    private final String name;
    private final String category;
    private final Long price;
    private final int stock;
    private final String status;
    private final LocalDateTime updatedAt;

    public static ProductUpdateResponse from(Product product) {
        return new ProductUpdateResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().getStatus(),
                product.getUpdatedAt()
        );
    }
}
