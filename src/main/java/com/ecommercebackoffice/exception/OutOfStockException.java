package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class OutOfStockException extends ServiceException {
    public OutOfStockException() {
        super(HttpStatus.BAD_REQUEST, "품절 상품은 주문할 수 없습니다.");
    }
}
