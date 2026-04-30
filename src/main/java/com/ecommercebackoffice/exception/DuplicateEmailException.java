package com.ecommercebackoffice.exception;

import com.ecommercebackoffice.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends ServiceException {
    // 생성자
    public DuplicateEmailException() {
        super(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
    }
}
