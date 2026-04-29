package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class ProductDuplicateException extends ServiceException {
    public ProductDuplicateException(String message) {
        super(HttpStatus.CONFLICT, "이미 등록된 상품입니다.");
    }
}
