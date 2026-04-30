package com.ecommercebackoffice.auth.controller;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.auth.service.AuthService;
import com.ecommercebackoffice.auth.dto.LoginRequest;
import com.ecommercebackoffice.common.dto.CommonResponse;
import com.ecommercebackoffice.auth.session.SessionAdmin;
import com.ecommercebackoffice.auth.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<String>> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        // 1. 인증 및 상태 검증 (실패 시 ServiceException 예외 발생)
        Admin admin = authService.authenticate(request.getEmail(), request.getPassword());

        // 2. 세션용 DTO 생성
        SessionAdmin sessionAdmin = new SessionAdmin(
                admin.getId(),
                admin.getEmail(),
                admin.getRole().name()
        );

        // 3. 세션 생성 및 저장 (SessionConst 상수 적용!)
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(SessionConst.LOGIN_ADMIN, sessionAdmin);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("로그인에 성공했습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 전체 무효화
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}