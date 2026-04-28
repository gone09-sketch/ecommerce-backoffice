package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.enums.AdminRole;
import lombok.Getter;

@Getter

public class AdminRolePatchRequest {
    // 속성
    private AdminRole role;

    // 생성자
    public AdminRolePatchRequest() {}
}
