package com.ecommercebackoffice.exception;

import com.ecommercebackoffice.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ServiceException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, "지원하지 않는 정렬 조건입니다.");
    }
}
