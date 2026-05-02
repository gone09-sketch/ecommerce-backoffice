package com.ecommercebackoffice.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {

    @NotNull(message = "고객 ID는 필수입니다.")
    private Long customerId;

    // NotEmpty는 리스트가 비어 있으면 예외처리로 감, null + 빈 리스트를 막음
    // 여기서 Valid는 리스트 안에 있는 OrderProductCreateRequest 객체 내부 검증도 실행하라는 의미
    @NotEmpty(message = "주문 상품은 최소 1개 이상이어야 합니다.")
    @Valid
    private List<OrderProductCreateRequest> products;

    @NotBlank(message = "수령인 이름은 필수입니다.")
    private String receiverName;

    @NotBlank(message = "수령인 연락처는 필수입니다.")
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "연락처는 010-0000-0000 형식으로 입력해주세요."
    )
    private String receiverPhone;

    @NotBlank(message = "배송지는 필수입니다.")
    private String deliveryAddress;
}
