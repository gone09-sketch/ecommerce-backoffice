package com.ecommercebackoffice.product.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class ProductStockUpdateRequest {

    @Min(0) // 최소값 0 설정
    private int stock;
}
