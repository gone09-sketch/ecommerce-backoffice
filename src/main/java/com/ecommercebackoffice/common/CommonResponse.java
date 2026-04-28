package com.ecommercebackoffice.common;

import lombok.Getter;

@Getter

public class CommonResponse<T> {
    // 속성
    private int httpStatus;
    private String message;
    private T data;

    // 생성자
    private CommonResponse(int httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    // 기능
    // 200 OK 성공 응답 (data 있을 경우)
    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>(200, message, data);
    }

    // 200 OK 성공 응답 (data 없을 경우)
    public static <T> CommonResponse<T> success(String message) {
        return new CommonResponse<>(200, message, null);
    }

    // 201 Created 성공 응답
    public static <T> CommonResponse<T> created(String message, T data) {
        return new CommonResponse<>(201, message, data);
    }

    // 실패
    public static <T> CommonResponse<T> fail(int httpStatus, String message) {
        return new CommonResponse<>(400, message, null);
    }
}
