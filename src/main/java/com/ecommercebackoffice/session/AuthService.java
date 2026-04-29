package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.config.PasswordEncoder; // PasswordEncoder 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; // 암호화 클래스 주입

    /**
     * DB를 조회하여 관리자 인증을 수행합니다.
     */
    public Admin authenticate(String email, String password) {

        // 1. 이메일로 DB에서 관리자 정보를 찾습니다.
        Admin admin = adminRepository.findByEmail(email).orElse(null);

        // 해당 이메일을 가진 관리자가 없으면 인증 실패
        if (admin == null) {
            return null;
        }

        // 2. 비밀번호 검증 (PasswordEncoder의 matches 메서드 사용!)
        // 입력받은 평문 비밀번호(password)와 DB에 저장된 암호화된 비밀번호(admin.getPassword())를 비교합니다.
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return null; // 비밀번호가 일치하지 않으면 인증 실패
        }

        // 3. 인증 성공 시 Admin 객체 반환
        return admin;
    }
}