package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends ServiceException {
    // 생성자
    public PasswordMismatchException() {
        super(HttpStatus.BAD_REQUEST, "새 비밀번화와 일치하지 않습니다.");
    }
}
