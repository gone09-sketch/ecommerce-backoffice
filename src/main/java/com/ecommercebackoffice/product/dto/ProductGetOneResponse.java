package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductGetOneResponse {

    private final String name;
    private final String category;
    private final Long price;
    private final int stock;
    private final String status;
    private final LocalDateTime createdAt;
    private final String adminName;
    private final String adminEmail;

    public static ProductGetOneResponse from(Product product) {
        return new ProductGetOneResponse(
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().getStatus(),
                product.getCreatedAt(),
                product.getAdmin().getName(),
                product.getAdmin().getEmail()
        );
    }
}
