package com.ecommercebackoffice.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public class ServiceException extends RuntimeException {
    // 속성
    private final HttpStatus status;

    // 생성자
    public ServiceException(HttpStatus status, String message) {
        super(message); // 부모 생성자 호출: 메세지는 RunTimeException에 저장
        this.status = status; // 상태는 여기에 저장
    }
}
