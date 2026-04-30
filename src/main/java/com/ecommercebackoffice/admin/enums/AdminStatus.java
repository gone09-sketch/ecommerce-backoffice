package com.ecommercebackoffice.admin.enums;

import com.ecommercebackoffice.exception.InvalidAdminStatusException;
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

    // 기능
    // 승인 가능 여부 (PENDING, REJECTED → ACTIVE)
    public void validateApprovable() {
        if (this != PENDING && this != REJECTED) {
            throw new InvalidAdminStatusException("승인이 가능한 상태가 아닙니다.");
        }
    }

    // 거부 가능 여부 (PENDING → REJECTED)
    public void validateRejectable() {
        if (this != PENDING) {
            throw new InvalidAdminStatusException("거부가 가능한 상태가 아닙니다.");
        }
    }
}
