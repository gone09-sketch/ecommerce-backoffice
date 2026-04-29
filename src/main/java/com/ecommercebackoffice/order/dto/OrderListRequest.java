package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.common.BasePageRequest;
import com.ecommercebackoffice.order.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListRequest extends BasePageRequest {
    private String keyword; // 주문번호, 고객명
    private OrderStatus status;  // 준비중, 배송중, 배송완료, 취소됨
}