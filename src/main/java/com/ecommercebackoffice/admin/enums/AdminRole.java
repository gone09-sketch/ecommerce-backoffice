package com.ecommercebackoffice.admin.enums;

import lombok.Getter;

@Getter

public enum AdminRole {

    // 속성
    SUPER_ADMIN("슈퍼 관리자"),
    OPERATION_ADMIN("운영 관리자"),
    CS_ADMIN("CS 관리자");

    private final String description;

    // 생성자
    AdminRole(String description) {
        this.description = description;
    }
}
