package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; //  반드시 이 줄이 import 되어야 합니다!

public interface AdminRepository extends JpaRepository<Admin, Long> {
    // 기존에 Admin findByEmail(...) 로 되어있던 것을 아래처럼 바꿉니다.
    Optional<Admin> findByEmail(String email);
}