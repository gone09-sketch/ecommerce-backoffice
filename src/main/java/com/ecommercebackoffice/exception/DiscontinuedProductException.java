package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class DiscontinuedProductException extends ServiceException {
    public DiscontinuedProductException() {
        super(HttpStatus.BAD_REQUEST, "단종 상품은 주문할 수 없습니다.");
    }
}
