package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin; // 👉 실제 Admin 패키지 경로로 수정
import com.ecommercebackoffice.admin.repository.AdminRepository; // (본인의 Repository 경로에 맞게 확인해주세요)
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;

    public Admin authenticate(String email, String password) {
        // 1. 이메일로 관리자 조회
        Admin admin = (Admin) adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 검증
        if (!admin.getPassword().equals(password)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 상태 검증 (AdminStatus Enum을 문자로 바꿔서 ACTIVE인지 확인)
        // PENDING(승인대기)이나 REJECTED(거부) 상태면 로그인 차단
        if (!"ACTIVE".equals(admin.getStatus().name())) {
            throw new IllegalStateException("승인되지 않았거나 정지된 계정입니다. 관리자에게 문의하세요.");
        }

        return admin;
    }
}