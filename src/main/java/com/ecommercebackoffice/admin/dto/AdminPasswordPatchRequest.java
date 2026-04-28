package com.ecommercebackoffice.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPasswordPatchRequest {
    // 속성
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
