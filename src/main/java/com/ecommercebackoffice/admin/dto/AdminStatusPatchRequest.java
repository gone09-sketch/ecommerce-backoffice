package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.enums.AdminStatus;
import lombok.Getter;

@Getter

public class AdminStatusPatchRequest {
    // 속성
    private AdminStatus status;

    // 생성자
    public AdminStatusPatchRequest() {}
}
