package com.ecommercebackoffice.admin.service;

import com.ecommercebackoffice.admin.dto.*;
import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.exception.AdminNotFoundException;
import com.ecommercebackoffice.exception.DuplicateEmailException;
import com.ecommercebackoffice.exception.InvalidInputException;
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
        // 1. 이메일 중복 체크
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
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 반환
        return AdminGetResponse.from(admin);
    }

    // 내 프로필 조회
    @Transactional(readOnly = true)
    public AdminProfileGetResponse getProfile(Long adminId) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 반환
        return AdminProfileGetResponse.from(admin);
    }

    // 관리자 정보 수정
    @Transactional
    public AdminPatchResponse patchAdmin(Long adminId, AdminPatchRequest adminPatchRequest) {
        // 1. 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 데이터 준비
        String newEmail = adminPatchRequest.getEmail();

        // 3. 이메일 변경 시 중복 체크 newEmail이 null이 아닐 경우,
        if (newEmail != null) {
            // 3-1. newEmail이 공백일 경우 예외처리
            if (newEmail.isBlank()) {
                throw new InvalidInputException("이메일은 공백일 수 없습니다.");
            }

            // 3-2. 이메일을 변경한 경우, 기존 이메일과 다르고 DB에 이미 존재하면 예외처리
            if (!newEmail.equals(foundAdmin.getEmail()) &&
            adminRepository.existsByEmail(newEmail)) {
                throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
            }
        }

        // 4. 수정 내용 업데이트 + 데이터 담아주기
        foundAdmin.adminUpdate(adminPatchRequest);

        // 5. 반환
        return AdminPatchResponse.from(foundAdmin);
    }

    // 내 프로필 수정
    @Transactional
    public AdminProfilePatchResponse patchProfile(Long adminId, AdminProfilePatchRequest profilePatchRequest) {
        // 1. 관리자 조회
        Admin foundProfile = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 데이터 준비
        String newEmail = profilePatchRequest.getEmail();

        // 3. 이메일 변경 시 중복 체크 newEmail이 null이 아닐 경우,
        if (newEmail != null) {
            // 3-1. newEmail이 공백일 경우 예외처리
            if (newEmail.isBlank()) {
                throw new InvalidInputException("이메일은 공백일 수 없습니다.");
            }

            // 3-2. 이메일을 변경한 경우, 기존 이메일과 다르고 DB에 이미 존재하면 예외처리
            if (!newEmail.equals(foundProfile.getEmail()) &&
                    adminRepository.existsByEmail(newEmail)) {
                throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
            }
        }

        // 3. 수정 내용 업데이트 + 데이터 담아주기
        foundProfile.profileUpdate(profilePatchRequest);

        // 4. 반환
        return AdminProfilePatchResponse.from(foundProfile);
    }

    // 내 비밀번호 변경

    // 관리자 역할 변경

    // 관리자 상태 변경

    // 관리자 삭제






}
