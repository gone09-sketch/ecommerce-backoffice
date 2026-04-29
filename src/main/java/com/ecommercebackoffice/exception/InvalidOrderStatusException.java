package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderStatusException extends ServiceException {
    public InvalidOrderStatusException() {
        super(HttpStatus.BAD_REQUEST, "주문 상태는 준비중 -> 배송중 -> 배송완료 순서로만 변경할 수 있습니다.");
    }
}
