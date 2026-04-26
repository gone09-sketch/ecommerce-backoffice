package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin; // 👉 실제 Admin 패키지 경로로 수정완료!
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        // 1. AuthService를 통해 실제 DB 데이터 검증
        Admin admin = authService.authenticate(request.getEmail(), request.getPassword());

        // --- 검증 성공 후 ---

        // 2. 통합 세션 객체 생성 (enum인 Role은 .name()을 붙여서 String으로 바꿔줍니다)
        SessionUser sessionUser = new SessionUser(
                admin.getId(),
                admin.getEmail(),
                admin.getName(),
                admin.getRole().name(), //  Enum 타입을 String으로 변환
                "ADMIN"
        );

        // 3. 세션 생성 및 데이터 저장
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("LOGIN_USER", sessionUser);

        return ResponseEntity.ok(admin.getName() + "님, 로그인에 성공했습니다.");
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