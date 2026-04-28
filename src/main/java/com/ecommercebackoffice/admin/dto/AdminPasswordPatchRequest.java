package com.ecommercebackoffice.admin.dto;

import lombok.Getter;

@Getter

public class AdminPasswordPatchRequest {
    // 속성
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    // 생성자
    public AdminPasswordPatchRequest() {}
}
