package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends ServiceException {

    // 생성자
    public AdminNotFoundException() {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다.");
    }
}
