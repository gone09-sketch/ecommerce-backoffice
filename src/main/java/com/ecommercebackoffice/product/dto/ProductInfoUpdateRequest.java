package com.ecommercebackoffice.product.dto;

import lombok.Getter;

@Getter
public class ProductInfoUpdateRequest {

    private String name;
    private String category;
    private Long price;
}
