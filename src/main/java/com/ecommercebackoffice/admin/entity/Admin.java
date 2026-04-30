package com.ecommercebackoffice.admin.entity;

import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.enums.AdminStatus;
import com.ecommercebackoffice.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Admin extends BaseEntity {
    // 속성
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column (nullable = false, unique = true)
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

    private LocalDateTime approvedAt; // 승인일
    private LocalDateTime rejectedAt; // 거부일
    private String rejectedReason; // 거부 사유


    // 생성자
    public Admin(String name, String email, String password, String phoneNumber, AdminRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = AdminStatus.PENDING;
    }


    // 기능
    // 등록 승인 시점에 승인일 자동 기록
    public void approve() {
        // 승인대기 혹은 거부상태에서만 승인 가능
        this.status.validateApprovable();

        this.status = AdminStatus.ACTIVE;
        this.approvedAt = LocalDateTime.now();

        this.rejectedAt = null;
        this.rejectedReason = null;
    }

    // 등록 거부 시점에 거부일 및 거부 사유
    public void reject(String rejectedReason) {
        // 승인 대기 상태에서만 거부 가능
        this.status.validateRejectable();

        this.status = AdminStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
        this.rejectedReason = rejectedReason;

        this.approvedAt = null;
    }

    // 관리자 및 내 프로필 수정 update
    public void update(String name, String email, String phoneNumber) {
        // null이 아닌 경우 수정, null인 경우 기존 데이터 유지
        if (name != null) {
            this.name = name;
        }
        if (email != null) {
            this.email = email;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
    }


    // 관리자 역할 update
    public void updateRole(AdminRole newRole) {
        this.role = newRole;
    }

    // 관리자 상태 update
    public void updateStatus(AdminStatus newStatus) {
        this.status = newStatus;
    }

    // 비밀번호 update
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
