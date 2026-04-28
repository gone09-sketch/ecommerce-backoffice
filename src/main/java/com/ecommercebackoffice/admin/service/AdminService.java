package com.ecommercebackoffice.admin.service;

import com.ecommercebackoffice.admin.dto.*;
import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.config.PasswordEncoder;
import com.ecommercebackoffice.exception.AdminNotFoundException;
import com.ecommercebackoffice.exception.DuplicateEmailException;
import com.ecommercebackoffice.exception.InvalidInputException;
import com.ecommercebackoffice.session.SessionAdmin;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@Getter

public class AdminService {
    private final PasswordEncoder passwordEncoder;
    // 속성
    private AdminRepository adminRepository;

    // 생성자
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
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

        // 3. db 저장
        adminRepository.save(newAdmin);

        // 4. 반환
        return AdminCreateResponse.from(newAdmin);
    }


    // 관리자 리스트 조회
    @Transactional(readOnly = true)
    public PageResponse<AdminPageListResponse> getList(AdminPageRequest adminPageRequest) {
        // 1. JPA pageable로 변환 (정렬순서, 정렬컬럼, 페이지 로직)
        Pageable pageable = adminPageRequest.toPageable();

        // 2. 검색어가 공백인 경우 전체 조회를 위해 null 처리
        String keyword;
        if (StringUtils.hasText(adminPageRequest.getKeyword())) {
            // 값이 있으면 → 앞뒤 공백 제거해서 저장
            keyword = adminPageRequest.getKeyword().trim();
        } else {
            // null이거나 빈 문자열이면 → null로 저장
            keyword = null;
        }

        // 3. DB에서 Admin 엔티티 목록 조회
        Page<Admin> adminPages = adminRepository.findAllWithFilters(
                keyword,
                adminPageRequest.getRole(),
                adminPageRequest.getStatus(),
                pageable
        );

        // 4. dto 변환
        Page<AdminPageListResponse> responsePage = adminPages.map(AdminPageListResponse::from);

        // 5. 최종반환
        return new PageResponse<>(responsePage);
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
    public AdminPatchResponse updateAdmin(Long adminId, AdminPatchRequest adminPatchRequest) {

        // 1. 관리자 조회
        Admin admin = findAdminById(adminId);

        // 2. 이메일 중복 확인
        validateEmail(adminPatchRequest.getEmail(), admin.getEmail());

        // 3. 수정 내용 업데이트
        admin.update(
                adminPatchRequest.getName(),
                adminPatchRequest.getEmail(),
                adminPatchRequest.getPhoneNumber());

        // 4. 반환
        return AdminPatchResponse.from(admin);
    }


    // 내 프로필 수정
    @Transactional
    public AdminProfilePatchResponse updateProfile(Long adminId, AdminProfilePatchRequest profilePatchRequest) {

        // 1. 관리자 조회
        Admin admin = findAdminById(adminId);

        // 2. 이메일 변경 시 중복 체크 (null이면 변경 안 하는 것으로 간주)
       validateEmail(profilePatchRequest.getEmail(), admin.getEmail());

        // 3. 수정 내용 업데이트
        admin.update(
                profilePatchRequest.getName(),
                profilePatchRequest.getEmail(),
                profilePatchRequest.getPhoneNumber());

        // 4. 반환
        return AdminProfilePatchResponse.from(admin);
    }


    // 관리자 UPDATE 통합 메서드(내부용)
    private Admin findAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));
    }

    private void validateEmail(String newEmail, String currentEmail) {
        if (newEmail == null) return;
        if (!newEmail.equals(currentEmail) && adminRepository.existsByEmail(newEmail)) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }
    }


    // 내 비밀번호 변경
    @Transactional
    public void updatePassword(AdminPasswordPatchRequest adminPasswordPatchRequest,
                              HttpSession httpSession) {
        // 1. 세션에서 adminId 가져오기
        SessionAdmin sessionAdmin = (SessionAdmin) httpSession.getAttribute("loginAdmin");
        Long adminId = sessionAdmin.getId();

        // 2. 해당 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 3. 기존 비밀번호 일치 확인
        if (!passwordEncoder.matches(adminPasswordPatchRequest.getCurrentPassword(), foundAdmin.getPassword())) {
            throw new InvalidInputException("현재 비밀번호와 일치하지 않습니다.");
        }

        // 4. 새 비밀번호와 다시 입력받은 비밀번호 일치 검증
        if (!adminPasswordPatchRequest.getNewPassword().equals(adminPasswordPatchRequest.getConfirmPassword())) {
            throw new InvalidInputException("새 비밀번화와 일치하지 않습니다.");
        }

        // 5. 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(adminPasswordPatchRequest.getNewPassword());

        // 6. 새 비밀번호 업데이트
        foundAdmin.updatePassword(encodedPassword);
    }


    // 관리자 역할 변경
    @Transactional
    public AdminRolePatchResponse updateRole(Long adminId, AdminRolePatchRequest adminRolePatchRequest) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 수정 내용 업데이트
        admin.updateRole(adminRolePatchRequest.getRole());

        // 3. 반환
        return AdminRolePatchResponse.from(admin);
    }


    // 관리자 상태 변경
    @Transactional
    public AdminStatusPatchResponse updateStatus(Long adminId, AdminStatusPatchRequest adminStatusPatchRequest) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 수정 내용 업데이트
        admin.updateStatus(adminStatusPatchRequest.getStatus());

        // 3. 반환
        return AdminStatusPatchResponse.from(admin);
    }


    // 관리자 삭제
    @Transactional
    public void deleteAdmin(Long adminId) {
        // 1. 관리자 조회
        Admin foundAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 삭제 업데이트
        adminRepository.delete(foundAdmin);
    }

    // 관리자 등록 승인
    @Transactional
    public AdminApproveCreateResponse adminApprove(Long adminId) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 승인 처리
        admin.approve();

        // 3. 반환
        return AdminApproveCreateResponse.from(admin);
    }


    // 관리자 등록 거부
    @Transactional
    public AdminRejectCreateResponse adminReject(Long adminId, AdminRejectCreateRequest adminRejectCreateRequest) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 거부 처리
        admin.reject(adminRejectCreateRequest.getRejectedReason());

        // 3. 반환
        return AdminRejectCreateResponse.from(admin);
    }
}
