package com.ecommercebackoffice.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter

public class AdminRejectCreateRequest {
    // 속성
    @NotBlank(message = "거부 사유를 입력해주세요.")
    private String rejectedReason;

    // 생성자
    public AdminRejectCreateRequest() {}
}
