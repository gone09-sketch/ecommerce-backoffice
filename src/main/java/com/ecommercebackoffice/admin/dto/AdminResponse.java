package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.entity.Admin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdminResponse {
    // 속성
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime approvedAt;
    private final LocalDateTime rejectedAt;
    private final String rejectedReason;


    // 기능
    public static AdminResponse from(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole().getDescription(),
                admin.getStatus().getDescription(),
                admin.getCreatedAt(),
                admin.getUpdatedAt(),
                admin.getApprovedAt(),
                admin.getRejectedAt(),
                admin.getRejectedReason()
        );
    }
}
