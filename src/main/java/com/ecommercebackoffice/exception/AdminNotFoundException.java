package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends ServiceException {

    // 생성자
    public AdminNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
