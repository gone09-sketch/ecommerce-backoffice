package com.ecommercebackoffice.product.dto;

import lombok.Getter;

@Getter
public class ProductUpdateRequest {

    private String name;
    private String category;
    private Long price;
}
