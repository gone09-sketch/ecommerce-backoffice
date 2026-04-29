package com.ecommercebackoffice.exception.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private int httpStatus;
    private String message;

    public ErrorResponse(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}