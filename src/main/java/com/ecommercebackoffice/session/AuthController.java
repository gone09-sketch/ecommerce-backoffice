package com.ecommercebackoffice.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // (참고용) 실제로는 AuthService 등을 주입받아 비밀번호를 검증해야 합니다.

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        // 1. 이메일과 비밀번호 검증 로직 (여기서는 통과했다고 가정합니다)
        // Admin admin = authService.authenticate(request.getEmail(), request.getPassword());

        // --- 검증 성공 후 ---

        // 2. 세션에 저장할 객체 생성 (실제로는 DB에서 가져온 admin 객체의 값을 넣습니다)
        SessionAdminDto sessionAdmin = new SessionAdminDto(
                1L, // 예시 ID
                request.getEmail(),
                "슈퍼 관리자" // 예시 역할
        );

        // 3. 세션 생성 및 데이터 저장
        // getSession(true) : 기존 세션이 있으면 반환하고, 없으면 새로 생성합니다.
        HttpSession session = httpRequest.getSession(true);

        // 세션에 "LOGIN_ADMIN"이라는 키로 DTO를 저장합니다.
        session.setAttribute("LOGIN_ADMIN", sessionAdmin);

        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        // getSession(false) : 기존 세션이 있으면 반환하고, 없으면 null을 반환합니다.
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션을 완전히 무효화(삭제) 합니다.
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}