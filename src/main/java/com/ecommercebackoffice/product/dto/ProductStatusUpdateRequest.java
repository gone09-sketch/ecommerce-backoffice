package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.product.enums.ProductEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductStatusUpdateRequest {

    @NotNull
    private ProductEnum status;
}
