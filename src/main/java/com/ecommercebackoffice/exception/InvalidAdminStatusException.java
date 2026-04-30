package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidAdminStatusException extends ServiceException {

    // 외부(AuthService)에서 던져주는 구체적인 메시지를 받아 부모에게 전달합니다.
    public InvalidAdminStatusException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}