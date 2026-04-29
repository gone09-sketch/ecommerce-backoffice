package com.ecommercebackoffice.admin.controller;

import com.ecommercebackoffice.admin.dto.*;
import com.ecommercebackoffice.admin.service.AdminService;
import com.ecommercebackoffice.auth.session.SessionConst;
import com.ecommercebackoffice.common.CommonResponse;
import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.auth.session.SessionAdmin;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {
    // 속성
    private final AdminService adminService;

    // 생성자
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 기능
    // 관리자 등록(회원가입)
    @PostMapping("/signUp")
    public ResponseEntity<CommonResponse<AdminCreateResponse>> signUpAPI(
            @RequestBody @Valid AdminCreateRequest adminCreateRequest) {

        AdminCreateResponse signUpResponseAPI = adminService.signUp(adminCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.created("관리자 등록 요청 성공", signUpResponseAPI));
    }


    // 관리자 리스트 조회(전체조회)
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<AdminPageListResponse>>> getListAPI(
            @ModelAttribute AdminPageListRequest adminPageListRequest) {

        PageResponse<AdminPageListResponse> getListResponseAPI = adminService.getList(adminPageListRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 리스트 조회 성공", getListResponseAPI));
    }


    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public ResponseEntity<CommonResponse<AdminResponse>> getOneAPI(@PathVariable Long adminId) {

        AdminResponse getOneResponseAPI = adminService.getOne(adminId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 상세 조회 성공", getOneResponseAPI));
    }


    // 내 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse<AdminResponse>> getProfileAPI(
            @RequestAttribute(SessionConst.CURRENT_ADMIN) SessionAdmin sessionAdminDto) {

        // 1. Interceptor가 request에 담아준 로그인 관리자 id 사용
        Long loginAdminId = sessionAdminDto.getId();

        // 2. 내 프로필 조회
        AdminResponse getProfileResponseAPI = adminService.getProfile(loginAdminId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("내 프로필 조회 성공", getProfileResponseAPI));
    }


    // 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<CommonResponse<AdminResponse>> updateAdminAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminPatchRequest adminPatchRequest) {

        AdminResponse updateAdminResponseAPI = adminService.updateAdmin(adminId, adminPatchRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 정보 수정 성공", updateAdminResponseAPI));
    }


    // 내 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<CommonResponse<AdminResponse>> updateProfileAPI(
            @RequestBody @Valid AdminProfilePatchRequest adminProfilePatchRequest,
            @RequestAttribute(SessionConst.CURRENT_ADMIN) SessionAdmin sessionAdminDto,
            HttpSession httpSession) {

        // 1. Interceptor가 request에 담아준 로그인 관리자 id 사용
        Long loginAdminId = sessionAdminDto.getId();

        // 2. 프로필 수정
        AdminResponse updateProfileResponseAPI = adminService.updateProfile(loginAdminId, adminProfilePatchRequest);

        // 3. 세션 업데이트
        httpSession.setAttribute("loginAdmin", new SessionAdmin(
                updateProfileResponseAPI.getId(),
                updateProfileResponseAPI.getEmail(),
                updateProfileResponseAPI.getRole()
        ));

        // 4. 반환
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("프로필 정보 수정 성공", updateProfileResponseAPI));
    }


    // 내 비밀번호 변경
    @PatchMapping("/profile/password")
    public ResponseEntity<CommonResponse<Void>> updatePasswordAPI(
            @RequestBody @Valid AdminPasswordPatchRequest adminPasswordPatchRequest,
            @RequestAttribute(SessionConst.CURRENT_ADMIN) SessionAdmin sessionAdminDto) {

        // 1. Interceptor가 request에 담아준 로그인 관리자 id 사용
        Long loginAdminId = sessionAdminDto.getId();

        // 2. 비밀번호 변경
        adminService.updatePassword(loginAdminId, adminPasswordPatchRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("비밀번호 변경 성공"));
    }


    // 관리자 역할 변경
    @PatchMapping("/{adminId}/role")
    public ResponseEntity<CommonResponse<AdminResponse>> updateRoleAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminRolePatchRequest adminRolePatchRequest) {

        AdminResponse updateRoleResponseAPI = adminService.updateRole(adminId, adminRolePatchRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 역할 변경 성공", updateRoleResponseAPI));
    }


    // 관리자 상태 변경
    @PatchMapping("/{adminId}/status")
    public ResponseEntity<CommonResponse<AdminResponse>> updateStatusAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminStatusPatchRequest adminStatusPatchRequest) {

        AdminResponse updateStatusResponseAPI = adminService.updateStatus(adminId, adminStatusPatchRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 상태 변경 성공", updateStatusResponseAPI));
    }


    // 관리자 삭제
    @DeleteMapping("/{adminId}")
    public ResponseEntity<CommonResponse<Void>> deleteAdminAPI(@PathVariable Long adminId) {

        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("비밀번호 변경 성공"));
    }


    // 관리자 등록 승인
    @PostMapping("/{adminId}/approve")
    public ResponseEntity<CommonResponse<AdminResponse>> approveAPI(@PathVariable Long adminId) {

        AdminResponse adminApproveResponseAPI = adminService.adminApprove(adminId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 등록 승인 성공", adminApproveResponseAPI));
    }


    // 관리자 등록 거부
    @PostMapping("/{adminId}/reject")
    public ResponseEntity<CommonResponse<AdminResponse>> rejectAPI(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminRejectCreateRequest adminRejectCreateRequest) {

        AdminResponse adminRejectResponseAPI = adminService.adminReject(adminId, adminRejectCreateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 등록 거부 성공", adminRejectResponseAPI));
    }
}
