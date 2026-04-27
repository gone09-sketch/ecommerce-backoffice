package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
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
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        // 1. AuthService를 통해 실제 DB 데이터 검증 (실패 시 아래 ExceptionHandler로 넘어감)
        Admin admin = authService.authenticate(request.getEmail(), request.getPassword());

        // 2. 통합 세션 객체 생성
        SessionUser sessionUser = new SessionUser(
                admin.getId(),
                admin.getEmail(),
                admin.getName(),
                admin.getRole().name(),
                "admin"
        );

        // 3. 세션 생성 및 데이터 저장
        // 이 과정에서 스프링이 자동으로 'JSESSIONID' 쿠키를 생성하여 응답 헤더에 담아줍니다.
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("loginUser", sessionUser);

        return ResponseEntity.ok(admin.getName() + "님, 로그인에 성공했습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate(); // 서버에서 세션 정보 삭제 (쿠키 무효화)
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // [로그인 실패 처리] Exception Handlers

    // 이메일 또는 비밀번호 불일치 에러 잡기 (HTTP 401 Unauthorized 반환)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadCredentials(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // 상태 불량(정지, 거부, 대기 등) 에러 잡기 (HTTP 403 Forbidden 반환)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleBadStatus(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}