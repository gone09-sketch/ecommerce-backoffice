package com.ecommercebackoffice.admin.service;

import com.ecommercebackoffice.admin.dto.AdminCreateRequest;
import com.ecommercebackoffice.admin.dto.AdminCreateResponse;
import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.exception.DuplicateEmailException;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter

public class AdminService {
    // 속성
    private AdminRepository adminRepository;

    // 생성자
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    // 기능
    // 관리자 등록(회원가입)
    @Transactional
    public AdminCreateResponse signUp(AdminCreateRequest adminCreateRequest) {
        // 1. 동일 이메일이 있는지 확인(중복회원가입) (검증)
        if (adminRepository.existsByEmail(adminCreateRequest.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }

        // 2. 엔티티 생성(+데이터 담기)
        Admin newAdmin = new Admin(
                adminCreateRequest.getName(),
                adminCreateRequest.getEmail(),
                adminCreateRequest.getPassword(),
                adminCreateRequest.getPhoneNumber(),
                adminCreateRequest.getRole()
        );

        // 3. dto 반환
        AdminCreateResponse createResponse = new AdminCreateResponse(
                newAdmin.getId(),
                newAdmin.getName(),
                newAdmin.getEmail(),
                newAdmin.getPhoneNumber(),
                newAdmin.getRole(),
                newAdmin.getCreatedAt()
        );
        return createResponse;
    }

}
