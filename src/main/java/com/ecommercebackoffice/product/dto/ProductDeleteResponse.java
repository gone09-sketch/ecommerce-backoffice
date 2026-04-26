package com.ecommercebackoffice.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductDeleteResponse {

    private final int status;
    private final String message;
}
