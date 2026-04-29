package com.ecommercebackoffice.exception.handler;

import com.ecommercebackoffice.common.dto.CommonResponse;
import com.ecommercebackoffice.exception.base.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<CommonResponse<Void>> handlerServiceException(ServiceException e) {

        // 1. 응답 내용 (메시지 받아오기)
        String message = e.getMessage();

        // 2. 상태코드 준비 (Header)
        HttpStatus status = e.getStatus();

        // 3. 바디에 담기
        CommonResponse<Void> body = CommonResponse.fail(status.value(), message);

        // 4. ResponseEntity 객체 생성 후 반환
        return new ResponseEntity<>(body, status);
    }

    // 요청 본문(JSON)을 읽을 수 없을 때 자동 발생함 (이넘 값이 아닐 때)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

        // 1. 상태코드 준비 (Header)
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // 2. 응답 내용 (Body)
        String message = "유효하지 않은 값입니다.";
        CommonResponse<Void> body = CommonResponse.fail(status.value(), message);

        // 4. ResponseEntity 객체 생성 후 반환
        return new ResponseEntity<>(body, status);
    }

    // @Valid 유효성 검사 실패 시 발생 (@NotBlank, @Email 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {

        // 1. 유효성 검사 실패한 필드 중 첫 번째 에러 메시지를 꺼낸다.
        String message = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        // 2. 실패 응답 바디를 공통 응답 형식으로 만든다.
        CommonResponse<Void> commonResponse = CommonResponse.fail(
                HttpStatus.BAD_REQUEST.value(),
                message
        );

        // 3. HTTP 상태 코드는 400 Bad Request로 내려준다.
        return ResponseEntity.badRequest().body(commonResponse);
    }

    // path 값 타입 불일치 예외
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e
    ) {
        // 1. path variable 또는 request parameter의 타입이 맞지 않을 때 400으로 응답한다.
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // 2. 실패 응답 바디를 공통 응답 형식으로 만든다.
        CommonResponse<Void> body = CommonResponse.fail(
                HttpStatus.BAD_REQUEST.value(),
                "잘못된 요청입니다."
        );

        return new ResponseEntity<>(body, status);
    }
}
