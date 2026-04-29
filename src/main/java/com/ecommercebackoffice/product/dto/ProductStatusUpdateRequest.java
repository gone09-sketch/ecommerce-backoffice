package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductStatusUpdateRequest {

    @NotNull
    private ProductStatus status;
}
