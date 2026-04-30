package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.config.PasswordEncoder;;
import com.ecommercebackoffice.exception.InvalidAdminStatusException;
import com.ecommercebackoffice.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin authenticate(String email, String password) {
        // 1. 이메일로 관리자 조회 (예외 발생 시 괄호 안을 비웁니다)

        // () -> new 클래스 형태보단 클래스: :new 더짧은 문법으로 대체 가능
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // 3. 관리자 상태 검증
        checkAdminStatus(admin); // 여기는 외부에서 메세지를 주입하므로 기존 코드 그대로 유지!

        return admin;
    }

    private void checkAdminStatus(Admin admin) {
        // 상태에 따라 외부에서 직접 예외 메시지를 주입하여 던집니다.
        switch (admin.getStatus()) {
            case PENDING: // 승인 대기
                throw new InvalidAdminStatusException("승인 대기 중인 계정입니다.");
            case REJECTED: // 거부
                throw new InvalidAdminStatusException("계정 신청이 거부되었습니다.");
            case SUSPENDED: // 정지
                throw new InvalidAdminStatusException("정지된 계정입니다.");
            case INACTIVE: // 비활성화
                throw new InvalidAdminStatusException("비활성화된 계정입니다.");
            case ACTIVE:
                // 활성 상태는 아무것도 하지 않고 통과
                break;
        }
    }
}