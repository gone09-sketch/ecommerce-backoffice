package com.ecommercebackoffice.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProductCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotBlank
    private Long price;
    @NotBlank
    private int stock;
}
