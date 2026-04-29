package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends ServiceException {
    // 생성자
    public InvalidPasswordException() {
        super(HttpStatus.BAD_REQUEST, "현재 비밀번호와 일치하지 않습니다.");
    }
}
