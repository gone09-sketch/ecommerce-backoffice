package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.enums.AdminRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter

public class AdminPatchRequest {
    // 속성
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "^01[016789]-?\\d{4}-?\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;


    // 생성자
    public AdminPatchRequest() {}
}
