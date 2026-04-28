package com.ecommercebackoffice.config;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (adminRepository.count() > 0) {
            return;
        }
        initAdmins();
    }

    private void initAdmins() {
        // ACTIVE 슈퍼관리자 (로그인 테스트용)
        Admin superAdmin = new Admin("슈퍼관리자", "super@test.com", passwordEncoder.encode("12345678"), "010-1234-0001", AdminRole.SUPER_ADMIN);
        superAdmin.approve();
        adminRepository.save(superAdmin);

        // PENDING 관리자 (승인/거부 테스트용)
        adminRepository.save(new Admin("CS관리자1", "cs1@test.com", "12345678", "010-1234-0002", AdminRole.CS_ADMIN));
        adminRepository.save(new Admin("CS관리자2", "cs2@test.com", "12345678", "010-1234-0003", AdminRole.CS_ADMIN));
        adminRepository.save(new Admin("운영관리자1", "operation1@test.com", "12345678", "010-1234-0004", AdminRole.OPERATION_ADMIN));
    }
}