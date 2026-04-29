package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotBlank
    private Long price;
    @Min(1)
    private int stock;
    @NotBlank
    private ProductStatus status;
}
