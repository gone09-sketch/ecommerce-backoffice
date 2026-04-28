package com.ecommercebackoffice.admin.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPatchRequest {
    // 속성
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "^01[016789]-?\\d{4}-?\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;
}
