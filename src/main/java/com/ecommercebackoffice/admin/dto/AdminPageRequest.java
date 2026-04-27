package com.ecommercebackoffice.admin.dto;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.enums.AdminStatus;
import com.ecommercebackoffice.common.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdminPageRequest extends BasePageRequest {
    // 속성
    private String keyword; // 이름 또는 이메일 검색
    private AdminRole role; // 역할 필터 (슈퍼 관리자, 운영 관리자, CS 관리자)
    private AdminStatus status; // 상태 필터 (활성, 비활성, 승인대기, 정지, 거부)
}
