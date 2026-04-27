package com.ecommercebackoffice.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductGetAllResponse {

    private final int status;
    private final String message;
    private final List<ProductGetAllResult> data;
}
