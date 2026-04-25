package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.common.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListRequest extends BasePageRequest {
    private String keyword;   // 상품명
    private String category;  // 전자기기, 패션/의류, 식품
    private String status;    // 판매중, 품절, 단종
}
