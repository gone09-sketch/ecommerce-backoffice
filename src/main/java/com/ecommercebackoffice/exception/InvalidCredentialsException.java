package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ServiceException {

    // 기본 생성자: 발생 시 고정된 메시지와 상태 코드를 부모에게 전달합니다.
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다.");
    }
}