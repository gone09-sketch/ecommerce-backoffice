package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class DuplicateProductException extends ServiceException {
    public DuplicateProductException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
