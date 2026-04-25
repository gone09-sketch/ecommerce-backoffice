package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends ServiceException {
    // 생성자
    public DuplicateEmailException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
