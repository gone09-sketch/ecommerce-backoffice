package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.common.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminListRequest extends BasePageRequest {
    private String keyword; // 이름, 이메일
    private String role;    // 슈퍼 관리자, 운영 관리자, CS 관리자
    private String status;  // 활성, 비활성, 정지, 승인대기, 거부
}