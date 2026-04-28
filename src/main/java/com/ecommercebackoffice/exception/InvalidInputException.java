package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends ServiceException {
    // 생성자
    public InvalidInputException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
