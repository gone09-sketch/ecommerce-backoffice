package com.ecommercebackoffice.admin.service;

import com.ecommercebackoffice.admin.dto.AdminCreateRequest;
import com.ecommercebackoffice.admin.dto.AdminCreateResponse;
import com.ecommercebackoffice.admin.dto.AdminGetResponse;
import com.ecommercebackoffice.admin.dto.AdminProfileGetResponse;
import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.exception.AdminNotFoundException;
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

        // 3. 반환
        return AdminCreateResponse.from(newAdmin);
    }


    // 관리자 상세 조회
    @Transactional(readOnly = true)
    public AdminGetResponse getOne(Long adminId) {
        // 1. 해당 관리자 Id 존재 유무 확인 (검증) + 데이터 가져와서 넣기
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 반환
        return AdminGetResponse.from(admin);
    }

    // 내 프로필 조회
    @Transactional(readOnly = true)
    public AdminProfileGetResponse getProfile(Long adminId) {
        // 1. 해당 Id 관리자 데이터 가져오기
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 반환
        return AdminProfileGetResponse.from(admin);
    }

    // 관리자 정보 수정

    // 내 프로필 수정

    // 내 비밀번호 변경

    // 관리자 역할 변경

    // 관리자 상태 변경

    // 관리자 삭제






}
