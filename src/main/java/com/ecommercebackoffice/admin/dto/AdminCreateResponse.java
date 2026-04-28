package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.enums.AdminStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter

public class AdminCreateResponse {
    // 속성
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;

    // 생성자
    public AdminCreateResponse(Long id, String name, String email, String phoneNumber, String role, String status,
                               LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    // 기능
    public static AdminCreateResponse from(Admin admin) {
        return new AdminCreateResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole().getDescription(),
                admin.getStatus().getDescription(),
                admin.getCreatedAt()
        );
    }
}
