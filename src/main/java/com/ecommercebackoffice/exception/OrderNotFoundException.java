package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ServiceException {
    public OrderNotFoundException() {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다.");
    }
}
