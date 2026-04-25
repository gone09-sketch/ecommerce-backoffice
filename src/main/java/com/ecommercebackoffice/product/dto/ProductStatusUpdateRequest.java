package com.ecommercebackoffice.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProductStatusUpdateRequest {

    @NotBlank
    private String status;
}
