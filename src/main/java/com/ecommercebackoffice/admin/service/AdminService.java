package com.ecommercebackoffice.admin.service;

import com.ecommercebackoffice.admin.dto.*;
import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.config.PasswordEncoder;
import com.ecommercebackoffice.exception.AdminNotFoundException;
import com.ecommercebackoffice.exception.DuplicateEmailException;
import com.ecommercebackoffice.exception.InvalidInputException;
import com.ecommercebackoffice.session.SessionAdminDto;
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
    public PageResponse<AdminPageListResponse> getList(AdminPageRequest pageRequest) {
        // 1. JPA pageable로 변환
        Pageable pageable = pageRequest.toPageable();

        // 2. 검색 시, 빈 문자열 → null
        String keyword;
        if (StringUtils.hasText(pageRequest.getKeyword())) {
            // 값이 있으면 → 앞뒤 공백 제거해서 저장
            keyword = pageRequest.getKeyword().trim();
        } else {
            // null이거나 빈 문자열이면 → null로 저장
            keyword = null;
        }

        // 3. DB에서 Admin 엔티티 목록 조회
        Page<Admin> adminPage = adminRepository.findAllWithFilters(
                keyword,
                pageRequest.getRole(),
                pageRequest.getStatus(),
                pageable
        );

        // 4. dto 변환
        Page<AdminPageListResponse> responsePage = adminPage.map(admin -> AdminPageListResponse.from(admin));

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
    public AdminPatchResponse patchAdmin(Long adminId, AdminPatchRequest adminPatchRequest,
                                         HttpSession httpSession) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
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
            if (!newEmail.equals(admin.getEmail()) &&
            adminRepository.existsByEmail(newEmail)) {
                throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
            }
        }

        // 4. 수정 내용 업데이트 + 데이터 담아주기
        admin.adminUpdate(
                adminPatchRequest.getName(),
                adminPatchRequest.getEmail(),
                adminPatchRequest.getPhoneNumber());

        // 5. 세션 업데이트
        httpSession.setAttribute("loginAdmin", new SessionAdminDto(
                admin.getId(),
                admin.getEmail(),
                admin.getRole().getDescription()
        ));

        // 6. 반환
        return AdminPatchResponse.from(admin);
    }


    // 내 프로필 수정
    @Transactional
    public AdminProfilePatchResponse patchProfile(AdminProfilePatchRequest profilePatchRequest,
                                                  HttpSession httpSession) {
        // 1. 세션에서 adminId 가져오기
        SessionAdminDto sessionAdmin = (SessionAdminDto) httpSession.getAttribute("loginAdmin");
        Long adminId = sessionAdmin.getId();

        // 2. 해당 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 3. 데이터 준비
        String newEmail = profilePatchRequest.getEmail();

        // 4. 이메일 변경 시 중복 체크 (null이면 변경 안 하는 것으로 간주)
        if (newEmail != null) {

            // 4-1. 공백 이메일 예외처리
            if (newEmail.isBlank()) {
                throw new InvalidInputException("이메일은 공백일 수 없습니다.");
            }

            // 4-2. 기존 이메일과 다르고 DB에 이미 존재하면 예외처리
            if (!newEmail.equals(admin.getEmail()) &&
                    adminRepository.existsByEmail(newEmail)) {
                throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
            }
        }

        // 5. 수정 내용 업데이트
        admin.profileUpdate(
                profilePatchRequest.getName(),
                profilePatchRequest.getEmail(),
                profilePatchRequest.getPhoneNumber());

        // 6. 세션 업데이트
        httpSession.setAttribute("loginAdmin", new SessionAdminDto(
                admin.getId(),
                admin.getEmail(),
                admin.getRole().getDescription()
        ));

        // 7. 반환
        return AdminProfilePatchResponse.from(admin);
    }


    // 내 비밀번호 변경
    @Transactional
    public void patchPassword(AdminPasswordPatchRequest adminPasswordPatchRequest,
                              HttpSession httpSession) {
        // 1. 세션에서 adminId 가져오기
        SessionAdminDto sessionAdmin = (SessionAdminDto) httpSession.getAttribute("loginAdmin");
        Long adminId = sessionAdmin.getId();

        // 2. 해당 관리자 조회
        Admin foudAdmin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 3. 기존 비밀번호 일치 확인
        if (!passwordEncoder.matches(adminPasswordPatchRequest.getCurrentPassword(), foudAdmin.getPassword())) {
            throw new InvalidInputException("현재 비밀번호와 일치하지 않습니다.");
        }

        // 4. 새 비밀번호와 다시 입력받은 비밀번호 일치 검증
        if (!adminPasswordPatchRequest.getNewPassword().equals(adminPasswordPatchRequest.getConfirmPassword())) {
            throw new InvalidInputException("새 비밀번화와 일치하지 않습니다.");
        }

        // 5. 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(adminPasswordPatchRequest.getNewPassword());

        // 6. 새 비밀번호 업데이트
        foudAdmin.passwordUpdate(encodedPassword);
    }


    // 관리자 역할 변경
    @Transactional
    public AdminRolePatchResponse patchRole(Long adminId, AdminRolePatchRequest adminRolePatchRequest) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 수정 내용 업데이트
        admin.roleUpdate(adminRolePatchRequest.getRole());

        // 3. 반환
        return AdminRolePatchResponse.from(admin);
    }


    // 관리자 상태 변경
    @Transactional
    public AdminStatusPatchResponse patchStatus(Long adminId, AdminStatusPatchRequest adminStatusPatchRequest) {
        // 1. 관리자 조회
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다."));

        // 2. 수정 내용 업데이트
        admin.statusUpdate(adminStatusPatchRequest.getStatus());

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
