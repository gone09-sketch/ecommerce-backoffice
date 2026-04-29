package com.ecommercebackoffice.exception.handler;

import com.ecommercebackoffice.exception.ServiceException;
import com.ecommercebackoffice.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handlerServiceException(ServiceException e) {

        // 1. 응답 내용 (메시지 받아오기)
        String message = e.getMessage();

        // 2. 상태코드 준비 (Header)
        HttpStatus status = e.getStatus();

        // 3. 바디에 담기
        ErrorResponse body = new ErrorResponse(status.value(), message);

        // 3. ResponseEntity 객체 생성
        ResponseEntity<ErrorResponse> exceptionResponse = new ResponseEntity<>(body, status);

        // 4. 반환
        return exceptionResponse;
    }

    // 요청 본문(JSON)을 읽을 수 없을 때 자동 발생함 (이넘 값이 아닐 때)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

        // 1. 상태코드 준비 (Header)
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // 2. 응답 내용 (Body)
        String message = "유효하지 않은 값입니다.";
        ErrorResponse body = new ErrorResponse(status.value(), message);

        // 3. ResponseEntity 객체 생성
        ResponseEntity<ErrorResponse> exceptionResponse = new ResponseEntity<>(body, status);

        // 4. 반환
        return exceptionResponse;
    }

    // @Valid 유효성 검사 실패 시 발생 (@NotBlank, @Email 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // path 값 타입 불일치 예외
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e
    ) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "잘못된 요청입니다."
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
