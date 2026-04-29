package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class OrderAlreadyCompletedException extends ServiceException {
    public OrderAlreadyCompletedException() {
        super(HttpStatus.BAD_REQUEST, "배송완료 상태의 주문은 변경할 수 없습니다.");
    }
}
