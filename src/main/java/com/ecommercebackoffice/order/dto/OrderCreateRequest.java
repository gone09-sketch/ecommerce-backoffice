package com.ecommercebackoffice.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
<<<<<<< HEAD
import jakarta.validation.constraints.Pattern;
=======
>>>>>>> dev
import lombok.Getter;

@Getter
public class OrderCreateRequest {

<<<<<<< HEAD
    @NotNull(message = "고객 ID는 필수입니다.")
    private Long customerId;

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotNull(message = "주문 수량은 필수입니다.")
    @Min(1)
    private Integer quantity;

    @NotBlank(message = "수령인 이름은 필수입니다.")
    private String receiverName;

    @NotBlank(message = "수령인 연락처는 필수입니다.")
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "연락처는 010-0000-0000 형식으로 입력해주세요."
    )
    private String receiverPhone;

    @NotBlank(message = "배송지는 필수입니다.")
=======
    @NotNull
    private Long customerId;
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    private Integer quantity;
    @NotBlank
    private String receiverName;
    @NotBlank
    private String receiverPhone;
    @NotBlank
>>>>>>> dev
    private String deliveryAddress;
}
