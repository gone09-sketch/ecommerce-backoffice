package com.ecommercebackoffice.exception;

public class InvalidCredentialsException extends RuntimeException { // 1. 클래스 여는 중괄호
    public InvalidCredentialsException(String message) { // 2. 생성자 여는 중괄호
        super(message);
    } // 3. 생성자 닫는 중괄호 (클래스를 닫는 중괄호가 없음!)
}