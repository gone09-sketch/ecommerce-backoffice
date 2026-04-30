package com.ecommercebackoffice.exception;

public class InvalidPageException extends RuntimeException {

    public InvalidPageException() {
        super("유효하지 않은 페이지입니다.");
    }

    public InvalidPageException(String message) {
        super(message);
    }
}