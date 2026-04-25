package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.enums.ProductEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.ecommercebackoffice.product.enums.ProductEnum.ON_SALE;

@Getter
@RequiredArgsConstructor
public class ProductCreateResponse {

    private final Long id;
    private final String name;
    private final String category;
    private final Long price;
    private final int stock;
    private final ProductEnum status = ON_SALE;
    private final LocalDateTime createdAt;
}
