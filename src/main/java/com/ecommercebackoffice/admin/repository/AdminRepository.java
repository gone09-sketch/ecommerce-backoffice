package com.ecommercebackoffice.admin.repository;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.enums.AdminStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // 중복 이메일 확인
    boolean existsByEmail(String email);

    // 관리자 리스트 필터 적용하기
    // 검색과 필터가 모두 없다면 전체조회
    @Query("SELECT a FROM Admin a WHERE " +

            /* keyword가 null이면 검색 조건 무시
             * name 혹은 email 중 포함된 것만 조회 */
            "(:keyword IS NULL OR a.name LIKE %:keyword% OR a.email LIKE %:keyword%) AND " +

            /* role과 status가 null이면 필터 무시
             * role과 status 필터된 것 조회 */
            "(:role IS NULL OR a.role = :role) AND " +
            "(:status IS NULL OR a.status = :status)")

    Page<Admin> findAllWithFilters(
            @Param("keyword") String keyword,
            @Param("role") AdminRole role,
            @Param("status") AdminStatus status,
            Pageable pageable);

    Optional<Object> finqdByEmail(String email);
}
