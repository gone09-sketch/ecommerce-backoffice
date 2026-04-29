package com.ecommercebackoffice.auth.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor // 모든 필드를 포함하는 생성자를 자동으로 만들어줍니다.
public class SessionAdmin implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;       // 고유 식별자
    private String email;  // 이메일
    private String role;   // 역할 (예: 슈퍼 관리자, 운영 관리자)
}