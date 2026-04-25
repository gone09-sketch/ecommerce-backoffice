package com.ecommercebackoffice.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductGetOneResponse {

    private final int httpStatus;
    private final String message;
    private final ProductGetOneResult data;
}
