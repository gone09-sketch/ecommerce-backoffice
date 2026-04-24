package com.ecommercebackoffice.exception.handler;

import com.ecommercebackoffice.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServiceException {

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
