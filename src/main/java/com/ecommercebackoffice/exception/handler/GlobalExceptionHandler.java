package com.ecommercebackoffice.exception.handler;

import com.ecommercebackoffice.exception.ServiceException;
import com.ecommercebackoffice.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handlerServiceException(ServiceException e) {

        // 1. 응답 내용 (Body)
        String message = e.getMessage();
        ErrorResponse body = new ErrorResponse(message);

        // 2. 상태코드 준비 (Header)
        HttpStatus status = e.getStatus();

        // 3. ResponseEntity 객체 생성
        ResponseEntity<ErrorResponse> exceptionResponse = new ResponseEntity<>(body, status);

        // 4. 반환
        return exceptionResponse;
    }

    // 요청 본문(JSON)을 읽을 수 없을 때 자동 발생함 (이넘 값이 아닐 때)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

        // 1. 응답 내용 (Body)
        String message = "유효하지 않은 값입니다.";
        ErrorResponse body = new ErrorResponse(message);

        // 2. 상태코드 준비 (Header)
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // 3. ResponseEntity 객체 생성
        ResponseEntity<ErrorResponse> exceptionResponse = new ResponseEntity<>(body, status);

        // 4. 반환
        return exceptionResponse;
    }
}
