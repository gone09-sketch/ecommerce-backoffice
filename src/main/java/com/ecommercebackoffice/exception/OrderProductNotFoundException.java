package com.ecommercebackoffice.exception;

import com.ecommercebackoffice.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class OrderProductNotFoundException extends ServiceException {
    public OrderProductNotFoundException() {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 주문 상품 정보입니다.");
    }
}
