package com.ecommercebackoffice.auth.controller;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.auth.service.AuthService;
import com.ecommercebackoffice.auth.dto.LoginRequest;
import com.ecommercebackoffice.auth.session.SessionAdmin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        // 1. 서비스 호출을 통한 인증
        Admin admin = authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        // 인증 실패 처리 (admin이 null인 경우)
        if (admin == null) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // 2. 세션용 DTO 생성
        // admin.getRole().name()이 오류가 난다면 admin.getRole().toString()을 사용해 보세요.
        SessionAdmin sessionAdmin = new SessionAdmin(
                admin.getId(),
                admin.getEmail(),
                admin.getRole().name()
        );

        // 3. 세션 생성 및 저장
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("loginAdmin", sessionAdmin);

        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}