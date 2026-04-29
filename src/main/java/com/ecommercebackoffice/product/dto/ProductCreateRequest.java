package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotNull
    @Positive
    private Long price;
    @Min(1)
    private int stock;
    @NotNull
    private ProductStatus status;
}
