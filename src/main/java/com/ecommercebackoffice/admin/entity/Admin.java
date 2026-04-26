package com.ecommercebackoffice.admin.entity;

import com.ecommercebackoffice.admin.dto.AdminPatchRequest;
import com.ecommercebackoffice.admin.dto.AdminProfilePatchRequest;
import com.ecommercebackoffice.admin.dto.AdminRolePatchRequest;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.enums.AdminStatus;
import com.ecommercebackoffice.config.BaseEntity;
import com.ecommercebackoffice.exception.InvalidAdminStatusException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "admins")
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
        // 승인(PENDING) 상태가 아닐 시 승인 불가
        if (this.status != AdminStatus.PENDING) {
            throw new InvalidAdminStatusException("승인 가능한 상태가 아닙니다.");
        }

        this.status = AdminStatus.ACTIVE;
        this.approvedAt = LocalDateTime.now();
    }

    // 등록 거부 시점에 거부일 및 거부 사유
    public void reject(String reason) {
        // 승인(PENDING) 상태가 아닐 시 거부 불가
        if (this.status != AdminStatus.PENDING) {
            throw new InvalidAdminStatusException("거부 가능한 상태가 아닙니다.");
        }

        this.status = AdminStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
        this.rejectedReason = reason;
    }

    // 관리자 수정 update
    public Admin adminUpdate(AdminPatchRequest adminPatchRequest) {
        // null이 아닌 경우 수정, null인 경우 기존 데이터 유지
        if (adminPatchRequest.getName() != null) {
            this.name = adminPatchRequest.getName();
        }
        if (adminPatchRequest.getEmail() != null) {
            this.email = adminPatchRequest.getEmail();
        }
        if (adminPatchRequest.getPhoneNumber() != null) {
            this.phoneNumber = adminPatchRequest.getPhoneNumber();
        }
        return this;
    }

    // 내 프로필 수정 update
    public Admin profileUpdate(AdminProfilePatchRequest profilePatchRequest) {
        // null이 아닌 경우 수정, null인 경우 기존 데이터 유지
        if (profilePatchRequest.getName() != null) {
            this.name = profilePatchRequest.getName();
        }
        if (profilePatchRequest.getEmail() != null) {
            this.email = profilePatchRequest.getEmail();
        }
        if (profilePatchRequest.getPhoneNumber() != null) {
            this.phoneNumber = profilePatchRequest.getPhoneNumber();
        }
        return this;
    }

    // 관리자 역할 update
    public void roleUpdate(AdminRole newRole) {
        this.role = newRole;
    }

    // 관리자 상태 변경

    // 관리자 삭제
}
