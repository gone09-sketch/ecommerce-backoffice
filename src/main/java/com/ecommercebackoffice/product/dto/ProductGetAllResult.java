package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.enums.ProductEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductGetAllResult {

    private final Long id;
    private final String name;
    private final String category;
    private final Long price;
    private final int stock;
    private final String status;
    private final LocalDateTime createdAt;
    private final String adminName;
}
