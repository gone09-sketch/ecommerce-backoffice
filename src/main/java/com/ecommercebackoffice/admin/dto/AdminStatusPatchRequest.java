package com.ecommercebackoffice.admin.dto;

import com.ecommercebackoffice.admin.enums.AdminStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminStatusPatchRequest {
    // 속성
    private AdminStatus status;
}
