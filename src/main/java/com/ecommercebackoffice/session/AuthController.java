package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.exception.InvalidCredentialsException;
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
        // 1. 서비스 호출
        // 인증 실패 시 서비스에서 InvalidCredentialsException을 던지므로
        // 이 지점 아래의 코드는 오직 '인증 성공' 시에만 실행됩니다.
        Admin admin = authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        // 2. 세션용 DTO 생성
        SessionAdmin sessionAdmin = new SessionAdmin(
                admin.getId(),
                admin.getEmail(),
                admin.getRole().name()
        );

        // 3. 세션 생성 및 데이터 저장
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

    /**
     * 해당 컨트롤러에서 발생하는 InvalidCredentialsException을 캐치하여
     * 예외 내부에 설정된 상태 코드와 메시지로 응답합니다.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException e) {
        // e.getStatus()와 e.getMessage()는 부모인 ServiceException에서 관리됩니다.
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
}