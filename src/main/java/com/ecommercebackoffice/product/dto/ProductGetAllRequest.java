package com.ecommercebackoffice.product.dto;

import com.ecommercebackoffice.common.dto.BasePageRequest;
import com.ecommercebackoffice.product.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductGetAllRequest extends BasePageRequest {

    private String keyword;   // 상품명 검색
    private String category;  // 카테고리 필터
    private ProductStatus status;    // 상태 필터
}
