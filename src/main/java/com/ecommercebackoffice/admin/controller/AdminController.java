package com.ecommercebackoffice.admin.controller;

import com.ecommercebackoffice.admin.dto.*;
import com.ecommercebackoffice.admin.service.AdminService;
import com.ecommercebackoffice.session.SessionAdminDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

        AdminGetResponse getOneResponseAPI = adminService.getOne(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(getOneResponseAPI);
    }


    // 내 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<AdminProfileGetResponse> getProfileAPI(HttpServletRequest httpServletRequest) {

        // 1. 세션에서 내 id 가져오기
        SessionAdminDto sessionAdminDto = (SessionAdminDto) httpServletRequest.getSession()
                .getAttribute("loginAdmin");

        // 2. 세션에서 꺼낸 id 조회
        AdminProfileGetResponse profileGetResponseAPI = adminService.getProfile(sessionAdminDto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(profileGetResponseAPI);
    }


    // 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<AdminPatchResponse> patchAPI(
            @PathVariable Long adminId,
            @Valid
            @RequestBody AdminPatchRequest adminPatchRequest,
            HttpSession httpSession) {

        AdminPatchResponse adminPatchResponseAPI = adminService.patchAdmin(adminId, adminPatchRequest, httpSession);
        return ResponseEntity.status(HttpStatus.OK).body(adminPatchResponseAPI);
    }


    // 내 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<AdminProfilePatchResponse> patchProfileAPI(
            @Valid
            @RequestBody AdminProfilePatchRequest adminProfilePatchRequest,
            HttpSession httpSession) {

        AdminProfilePatchResponse profilePatchResponseAPI = adminService.patchProfile(adminProfilePatchRequest, httpSession);
        return ResponseEntity.status(HttpStatus.OK).body(profilePatchResponseAPI);
    }


    // 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<AdminRolePatchResponse> patchRoleAPI(
            @PathVariable Long adminId,
            @Valid
            @RequestBody AdminRolePatchRequest adminRolePatchRequest) {

        AdminRolePatchResponse rolePatchResponseAPI = adminService.patchRole(adminId, adminRolePatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(rolePatchResponseAPI);
    }


    // 관리자 상태 변경
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<AdminStatusPatchResponse> patchStatusAPI(
            @PathVariable Long adminId,
            @Valid
            @RequestBody AdminStatusPatchRequest adminStatusPatchRequest) {

        AdminStatusPatchResponse statusPatchResponseAPI = adminService.patchStatus(adminId, adminStatusPatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(statusPatchResponseAPI);
    }


    // 관리자 삭제
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAPI(@PathVariable Long adminId) {

        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
