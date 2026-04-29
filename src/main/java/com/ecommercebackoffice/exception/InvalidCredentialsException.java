package com.ecommercebackoffice.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ServiceException {

    // 기본 생성자
    public InvalidCredentialsException() {
        // 부모인 ServiceException에게 상태 코드와, 고정된 에러 메시지를 넘겨줍니다.
        super(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다.");
    }
}