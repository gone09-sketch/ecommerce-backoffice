package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.enums.AdminRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminRolePatchRequest {
    // 속성
    private AdminRole role;
}
