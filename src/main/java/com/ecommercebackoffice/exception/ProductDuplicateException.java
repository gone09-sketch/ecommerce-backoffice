package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class ProductDuplicateException extends ServiceException {
    public ProductDuplicateException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
