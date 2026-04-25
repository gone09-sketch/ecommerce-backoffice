package com.ecommercebackoffice.admin.controller;

import com.ecommercebackoffice.admin.dto.AdminCreateRequest;
import com.ecommercebackoffice.admin.dto.AdminCreateResponse;
import com.ecommercebackoffice.admin.dto.AdminGetResponse;
import com.ecommercebackoffice.admin.service.AdminService;
import com.ecommercebackoffice.exception.UnauthorizedException;
import com.ecommercebackoffice.session.SessionUser;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Getter
@RequestMapping("/admins")

public class AdminController {
    // 속성
    private AdminService adminService;

    // 생성자
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 기능
    // 관리자 등록(회원가입)
    @PostMapping("/signUp")
    public ResponseEntity<AdminCreateResponse> signUpAPI(
            @Valid
            @RequestBody AdminCreateRequest adminCreateRequest) {

        AdminCreateResponse signUpResponseAPI = adminService.signUp(adminCreateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(signUpResponseAPI);
    }

    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminGetResponse> getOneAPI(@PathVariable Long adminId) {

        AdminGetResponse getOneResponse = adminService.getOne(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(getOneResponse);
    }

    // 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<AdminPatchResponse> patchAPI(
            @PathVariable Long adminId,
            @Valid
            @RequestBody AdminPatchRequest adminPatchRequest) {



    }

    // 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<AdminPatchRoleResponse> patchRoleAPI(@PathVariable Long adminId) {

    }

    // 관리자 상태 변경
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<AdminPatchStatusResponse> patchStatusAPI(@PathVariable Long adminId) {

    }

    // 관리자 삭제
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAPI(@PathVariable Long adminId) {

    }
}
