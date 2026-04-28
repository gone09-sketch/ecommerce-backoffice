package com.ecommercebackoffice.admin.controller;

import com.ecommercebackoffice.admin.dto.*;
import com.ecommercebackoffice.admin.service.AdminService;
import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.session.SessionAdmin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
            @RequestBody @Valid AdminCreateRequest adminCreateRequest) {

        AdminCreateResponse signUpResponseAPI = adminService.signUp(adminCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponseAPI);
    }


    // 관리자 리스트 조회(전체조회)
    @GetMapping
    public ResponseEntity<PageResponse<AdminPageListResponse>> getListAPI(@ModelAttribute AdminPageRequest pageRequest) {

        PageResponse<AdminPageListResponse> data = adminService.getList(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
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
        SessionAdmin sessionAdmin = (SessionAdmin) httpServletRequest.getSession()
                .getAttribute("loginAdmin");

        // 2. 세션에서 꺼낸 id 조회
        AdminProfileGetResponse profileGetResponseAPI = adminService.getProfile(sessionAdmin.getId());
        return ResponseEntity.status(HttpStatus.OK).body(profileGetResponseAPI);
    }


    // 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<AdminPatchResponse> patchAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminPatchRequest adminPatchRequest,
            HttpSession httpSession) {

        AdminPatchResponse adminPatchResponseAPI = adminService.patchAdmin(adminId, adminPatchRequest, httpSession);
        return ResponseEntity.status(HttpStatus.OK).body(adminPatchResponseAPI);
    }


    // 내 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<AdminProfilePatchResponse> patchProfileAPI(
            @RequestBody @Valid AdminProfilePatchRequest adminProfilePatchRequest,
            HttpSession httpSession) {

        // 세션에서 adminId 가져오기
        SessionAdmin sessionAdmin = (SessionAdmin) httpSession.getAttribute("loginAdmin");
        Long adminId = sessionAdmin.getId();

        // 프로필 수정
        AdminProfilePatchResponse profilePatchResponseAPI = adminService.patchProfile(adminId, adminProfilePatchRequest);

        // 세션 업데이트
        httpSession.setAttribute("loginAdmin", new SessionAdmin(
                profilePatchResponseAPI.getId(),
                profilePatchResponseAPI.getEmail(),
                profilePatchResponseAPI.getRole()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(profilePatchResponseAPI);
    }

    // 내 비밀번호 변경
    @PatchMapping("/profile/password")
    public ResponseEntity<Void> patchPassword(@RequestBody @Valid AdminPasswordPatchRequest adminPasswordPatchRequest,
                                              HttpSession httpSession) {

        adminService.patchPassword(adminPasswordPatchRequest, httpSession);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<AdminRolePatchResponse> patchRoleAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminRolePatchRequest adminRolePatchRequest) {

        AdminRolePatchResponse rolePatchResponseAPI = adminService.patchRole(adminId, adminRolePatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(rolePatchResponseAPI);
    }


    // 관리자 상태 변경
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<AdminStatusPatchResponse> patchStatusAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminStatusPatchRequest adminStatusPatchRequest) {

        AdminStatusPatchResponse statusPatchResponseAPI = adminService.patchStatus(adminId, adminStatusPatchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(statusPatchResponseAPI);
    }


    // 관리자 삭제
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAPI(@PathVariable Long adminId) {

        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // 관리자 등록 승인
    @PostMapping("/{adminId}/approve")
    public ResponseEntity<AdminApproveCreateResponse> approveAPI(@PathVariable Long adminId) {

        AdminApproveCreateResponse approveResponseAPI = adminService.adminApprove(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(approveResponseAPI);
    }


    // 관리자 등록 거부
    @PostMapping("/{adminId}/reject")
    public ResponseEntity<AdminRejectCreateResponse> rejectAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminRejectCreateRequest adminRejectCreateRequest) {

        AdminRejectCreateResponse rejectResponseAPI = adminService.adminReject(adminId, adminRejectCreateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(rejectResponseAPI);
    }
}
