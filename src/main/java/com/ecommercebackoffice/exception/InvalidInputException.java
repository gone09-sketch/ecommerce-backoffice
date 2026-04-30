package com.ecommercebackoffice.exception;

import com.ecommercebackoffice.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class InvalidInputException extends ServiceException {
    // 생성자
    public InvalidInputException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 고객 상태입니다.");
    }
}
