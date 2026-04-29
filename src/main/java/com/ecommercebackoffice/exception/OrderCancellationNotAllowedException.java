package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class OrderCancellationNotAllowedException extends ServiceException {
    public OrderCancellationNotAllowedException() {
        super(HttpStatus.BAD_REQUEST, "준비중 상태의 주문만 취소할 수 있습니다.");
    }
}
