package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AdminRepository adminRepository;

    public Admin authenticate(String email, String password) {
        // 1. 이메일로 관리자 조회 (Optional 상자에서 꺼내고, 없으면 에러 던지기)
        Admin admin = (Admin) adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 검증
        if (!admin.getPassword().equals(password)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 상태 검증
        String status = admin.getStatus().name();

        if (!"ACTIVE".equals(status)) {
            switch (status) {
                case "PENDING":
                    throw new IllegalStateException("계정 승인대기 중입니다.");
                case "REJECTED":
                    String reason = admin.getRejectedReason() != null ? " 사유: " + admin.getRejectedReason() : "";
                    throw new IllegalStateException("계정 신청이 거부되었습니다." + reason);
                case "SUSPENDED":
                    throw new IllegalStateException("계정이 정지되었습니다.");
                case "INACTIVE":
                    throw new IllegalStateException("계정이 비활성화되었습니다.");
                default:
                    throw new IllegalStateException("로그인할 수 없는 계정 상태입니다.");
            }
        }

        return admin;
    }
}