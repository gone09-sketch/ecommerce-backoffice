package com.ecommercebackoffice.session;

import com.ecommercebackoffice.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // 반드시 필요

public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Optional<Admin> 을 반환하도록 설정
    Optional<Admin> findByEmail(String email);
}