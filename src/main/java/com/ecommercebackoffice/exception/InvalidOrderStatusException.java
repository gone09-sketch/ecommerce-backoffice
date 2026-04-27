package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderStatusException extends ServiceException {
    public InvalidOrderStatusException(String message) {
        super(HttpStatus.BAD_REQUEST,message);
    }
}
