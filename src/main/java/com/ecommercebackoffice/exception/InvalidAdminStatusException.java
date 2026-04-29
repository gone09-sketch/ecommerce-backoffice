package com.ecommercebackoffice.exception;

import com.ecommercebackoffice.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class InvalidAdminStatusException extends ServiceException {
    // 생성자
    public InvalidAdminStatusException() {
        super(HttpStatus.BAD_REQUEST, "승인/거부 가능한 상태가 아닙니다.");
    }
}
