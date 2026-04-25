package com.ecommercebackoffice.order.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderGetResponse {

    private final String orderNumber;
    private final String customerName;
    private final String customerEmail;
    private final String productName;
    private final Integer quantity;
    private final Long totalPrice;
    private final LocalDateTime createdAt;
    private final String status;
    private final String adminName;
    private final String adminEmail;
    private final String adminRole;

    public OrderGetResponse(String orderNumber, String customerName, String customerEmail, String productName, Integer quantity, Long totalPrice, LocalDateTime createdAt, String status, String adminName, String adminEmail, String adminRole) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminRole = adminRole;
    }
}
