package com.ecommercebackoffice.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductInfoUpdateRequest {

    private String name;
    private String category;
    private Long price;
}
