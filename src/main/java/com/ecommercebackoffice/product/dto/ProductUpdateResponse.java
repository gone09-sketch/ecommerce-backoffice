package com.ecommercebackoffice.product.dto;

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
    private final LocalDateTime updatedAt;
}
