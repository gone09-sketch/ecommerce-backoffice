package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;

    public Admin authenticate(String email, String password) {
        // 1. 이메일로 관리자 조회
        Admin admin = (Admin) adminRepository.finqdByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 검증
        if (!admin.getPassword().equals(password)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 상태 검증 (요구사항 반영: 활성 상태만 통과, 나머지는 구체적인 에러 메시지 반환)
        String status = admin.getStatus().name();

        if (!"ACTIVE".equals(status)) {
            switch (status) {
                case "PENDING":
                    throw new IllegalStateException("계정 승인대기 중입니다.");
                case "REJECTED":
                    // 거부 사유가 있다면 함께 보여주면 더 좋습니다.
                    String reason = admin.getRejectedReason() != null ? " 사유: " + admin.getRejectedReason() : "";
                    throw new IllegalStateException("계정 신청이 거부되었습니다." + reason);
                case "SUSPENDED": // (참고: AdminStatus Enum에 SUSPENDED가 추가되어야 합니다)
                    throw new IllegalStateException("계정이 정지되었습니다.");
                case "INACTIVE":  // (참고: AdminStatus Enum에 INACTIVE가 추가되어야 합니다)
                    throw new IllegalStateException("계정이 비활성화되었습니다.");
                default:
                    throw new IllegalStateException("로그인할 수 없는 계정 상태입니다.");
            }
        }

        // 모든 검증을 통과한 '활성(ACTIVE)' 관리자만 반환
        return admin;
    }
}