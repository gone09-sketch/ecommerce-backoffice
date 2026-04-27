package com.ecommercebackoffice.exception.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    // 속성
    private final String message;

    // 생성자
    public ErrorResponse(String message) {
        this.message = message;
    }
}
