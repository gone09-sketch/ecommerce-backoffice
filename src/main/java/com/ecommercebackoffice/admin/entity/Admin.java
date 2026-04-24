package com.ecommercebackoffice.admin.entity;

import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.enums.AdminStatus;
import com.ecommercebackoffice.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "admin_id")
@NoArgsConstructor

public class Admin extends BaseEntity {
    // 속성
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column (nullable = false)
    private String email;
    @Column (nullable = false)
    private String password;
    @Column (nullable = false)
    private String phoneNumber;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private AdminStatus status;


    private LocalDateTime approvedAt; // 등록 승인일
    private LocalDateTime rejectedAt; // 거부일
    private String rejectedReason; // 거부 사유


    // 생성자
    public Admin(String name, String email, String password, String phoneNumber, AdminRole role, AdminStatus status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }

    // 기능
    // 등록 승인 시점에 승인일 자동 기록
    public void approve() {
        this.status = AdminStatus.ACTIVE;
        this.approvedAt = LocalDateTime.now();
    }

    // 등록 거부 시점에 거부일 및 거부 사유 기록
    public void reject(String reason) {
        this.status = AdminStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
        this.rejectedReason = reason;
    }
}
