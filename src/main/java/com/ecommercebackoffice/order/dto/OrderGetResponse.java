package com.ecommercebackoffice.order.dto;

import jakarta.persistence.Column;
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
}
