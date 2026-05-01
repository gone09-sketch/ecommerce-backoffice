package com.ecommercebackoffice.exception;

import com.ecommercebackoffice.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends ServiceException {

    public CustomerNotFoundException() {
        super(HttpStatus.NOT_FOUND,"존재하지 않는 고객입니다");
    }
}
