package com.ecommercebackoffice.admin.dto;

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
    private final AdminRole role;
    private final AdminStatus status;
    private final LocalDateTime createdAt;

    // 생성자
    public AdminCreateResponse(Long id, String name, String email, String phoneNumber,
                              AdminRole role, LocalDateTime createdAt ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = AdminStatus.PENDING;
        this.createdAt = createdAt;




    }
}
