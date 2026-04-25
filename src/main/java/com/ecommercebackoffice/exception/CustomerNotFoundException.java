package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends ServiceException {

    public CustomerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND,message);
    }
}
