package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServiceException {

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
    }
}
