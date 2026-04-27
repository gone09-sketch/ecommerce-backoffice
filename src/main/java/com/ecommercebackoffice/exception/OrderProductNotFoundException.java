package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class OrderProductNotFoundException extends ServiceException{
    public OrderProductNotFoundException(String message) {super(HttpStatus.NOT_FOUND, message);}
}
