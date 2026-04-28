package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.enums.AdminRole;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter

public class AdminCreateRequest {
    // 속성
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상으로 입력해주세요.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^01[016789]-?\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;

    @NotNull(message = "관리자 역할을 선택해주세요.")
    private AdminRole role;

    // 생성자
    public AdminCreateRequest() {}
}
