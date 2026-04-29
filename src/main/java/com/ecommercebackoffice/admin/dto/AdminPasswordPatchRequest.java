package com.ecommercebackoffice.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPasswordPatchRequest {
    // 속성
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요,")
    private String newPassword;

    @NotBlank(message = "새 비밀번호를 다시 입력해주세요.")
    private String confirmPassword;
}
