package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.common.BasePageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerListRequest extends BasePageRequest {
    private String keyword; // 이름, 이메일
    private String status;  // 활성, 비활성, 정지
}