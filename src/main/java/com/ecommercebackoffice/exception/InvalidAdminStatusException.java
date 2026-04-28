package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidAdminStatusException extends ServiceException {
    // 생성자
    public InvalidAdminStatusException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
