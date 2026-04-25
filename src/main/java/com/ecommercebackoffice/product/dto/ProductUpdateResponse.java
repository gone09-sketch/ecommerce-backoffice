package com.ecommercebackoffice.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductUpdateResponse {

    private final int httpStatus;
    private final String message;
    private final ProductUpdateResult data;
}
