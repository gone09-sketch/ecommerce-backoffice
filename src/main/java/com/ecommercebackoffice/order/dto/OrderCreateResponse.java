package com.ecommercebackoffice.order.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderCreateResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final String orderNumber;
<<<<<<< HEAD
    private final String status;            // 응답에는 "준비중"을 보여주고 서비스 로직은 READY 설명값으로 꺼냄
=======
    private final String status;
>>>>>>> dev
    private final Integer quantity;
    private final Long orderPrice;
    private final Long totalPrice;
    private final Long adminId;

    public OrderCreateResponse(Long id, LocalDateTime createdAt, String orderNumber, String status,Integer quantity, Long orderPrice, Long totalPrice, Long adminId) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderNumber = orderNumber;
        this.status = status;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.totalPrice = totalPrice;
        this.adminId = adminId;
    }
}
