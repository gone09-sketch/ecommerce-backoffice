package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.exception.InvalidAdminStatusException;
import com.ecommercebackoffice.exception.InvalidCredentialsException;
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
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        // 인증 및 상태 검증 (실패 시 예외 발생)
        Admin admin = authService.authenticate(request.getEmail(), request.getPassword());

        SessionAdmin sessionAdmin = new SessionAdmin(
                admin.getId(),
                admin.getEmail(),
                admin.getRole().name()
        );

        // 세션 생성 및 저장 (세션 ID는 자동으로 쿠키에 담겨 전달됩니다)
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("loginAdmin", sessionAdmin);

        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    // [예외 처리] 이메일/비밀번호 불일치 (401 Unauthorized)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // [예외 처리] 계정 상태 이상 (403 Forbidden)
    @ExceptionHandler(InvalidAdminStatusException.class)
    public ResponseEntity<String> handleInvalidAdminStatus(InvalidAdminStatusException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
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