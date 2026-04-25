package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class AdminGetResponse {
    // 속성
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 생성자
    public AdminGetResponse(Long id, String name, String email, String phoneNumber,
                            String role, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 기능
    // from에서 변환 처리
    public static AdminGetResponse from(Admin admin) {
        return new AdminGetResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole().name(),
                admin.getStatus().name(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
        );
    }
}
