package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends ServiceException {
    public InsufficientStockException() {
        super(HttpStatus.BAD_REQUEST, "재고가 부족합니다");
    }
}
