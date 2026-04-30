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

    // 관리자 목록 조회 - 검색어/역할/상태 필터 조합 지원 (모두 null이면 전체 조회)
    @Query("""
            select a
            from Admin a
            where
              /* 이름 · 이메일 부분 일치 검색 */
              (:keyword is null or a.name like %:keyword% or a.email like %:keyword%)
              /* 역할/상태 필터가 있으면 정확히 일치하는 것만 조회 */
              and (:role is null or a.role = :role)
              and (:status is null or a.status = :status)
        """)
    Page<Admin> findAllWithFilters(
            @Param("keyword") String keyword,
            @Param("role") AdminRole role,
            @Param("status") AdminStatus status,
            Pageable pageable);


    // 이메일로 찾기
    Optional<Admin> findByEmail(String email);
}
