package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.entity.Admin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdminCreateResponse {
    // 속성
    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;


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
