package com.ecommercebackoffice.product.enums;

import lombok.Getter;

@Getter
public enum ProductEnum {
    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    DISCONTINUED("단종");

    private String status;

    ProductEnum(String status) {
        this.status = status;
    }
}
