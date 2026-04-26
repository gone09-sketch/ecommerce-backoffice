package com.ecommercebackoffice.admin.repository;

import com.ecommercebackoffice.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Object> findByEmail(String email);
}
