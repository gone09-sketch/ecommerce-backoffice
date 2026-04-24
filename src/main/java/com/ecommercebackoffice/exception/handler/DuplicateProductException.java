package com.ecommercebackoffice.exception.handler;

import com.ecommercebackoffice.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class DuplicateProductException extends ServiceException {
    public DuplicateProductException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
