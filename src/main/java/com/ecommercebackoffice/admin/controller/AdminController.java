package com.ecommercebackoffice.admin.controller;

import com.ecommercebackoffice.admin.dto.AdminCreateRequest;
import com.ecommercebackoffice.admin.dto.AdminCreateResponse;
import com.ecommercebackoffice.admin.dto.AdminGetResponse;
import com.ecommercebackoffice.admin.service.AdminService;
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
    @PostMapping("/signup")
    public ResponseEntity<AdminCreateResponse> signUpAPI(
            @Valid
            @RequestBody AdminCreateRequest adminCreateRequest) {

        AdminCreateResponse signUpResponseAPI = adminService.signUp(adminCreateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(signUpResponseAPI);
    }

    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminGetResponse> getOneAPI(
            @SessionAttribute(name = "loginAdmin", required = false) SessionUser sessionUser,
            @RequestParam("adminId") Long adminId) {

        AdminGetResponse getOneResponse = adminService.getOne(adminId);
        return ResponseEntity.status(HttpStatus.OK).body(getOneResponse);
    }


}
