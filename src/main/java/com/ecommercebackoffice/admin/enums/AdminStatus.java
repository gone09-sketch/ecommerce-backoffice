package com.ecommercebackoffice.admin.enums;

import lombok.Getter;

@Getter

public enum AdminStatus {
    // 속성
    ACTIVE("활성"),
    INACTIVE("비활성"),
    PENDING("승인대기"),
    SUSPENDED("정지"),
    REJECTED("거부");

    private final String description;

    // 생성자
    AdminStatus(String description) {
        this.description = description;
    }
}
