package com.ecommercebackoffice.customer.dto;

import com.ecommercebackoffice.customer.enums.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CustomerStatusUpdateRequest {
    @NotBlank(message = "고객 상태는 필수입니다.")
    private CustomerStatus status;
}
