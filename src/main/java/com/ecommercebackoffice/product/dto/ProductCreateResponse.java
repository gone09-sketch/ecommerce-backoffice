package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductCreateResponse {

    private final Long id;
    private final String name;
    private final String category;
    private final Long price;
    private final int stock;
    private final String status;
    private final LocalDateTime createdAt;

    public static ProductCreateResponse from(Product product) {
        return new ProductCreateResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().getStatus(),
                product.getCreatedAt()
        );
    }
}
