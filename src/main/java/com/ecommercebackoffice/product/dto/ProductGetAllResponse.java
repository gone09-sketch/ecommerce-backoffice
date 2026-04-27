package com.ecommercebackoffice.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductGetAllResponse {

    private final Long id;
    private final String name;
    private final String category;
    private final Long price;
    private final int stock;
    private final String status;
    private final LocalDateTime createdAt;
    private final String adminName;
}
