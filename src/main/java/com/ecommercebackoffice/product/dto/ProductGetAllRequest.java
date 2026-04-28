package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.common.BasePageRequest;
import com.ecommercebackoffice.product.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductGetAllRequest extends BasePageRequest {

    private String keyword;   // 상품명 검색
    private String category;  // 카테고리 필터
    private ProductStatus status;    // 상태 필터
}
