package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderGetResponse {

    private final String orderNumber;
    private final String customerName;
    private final String customerEmail;
    private final List<OrderProductResponse> products;
    private final String receiverName;
    private final String receiverPhone;
    private final String deliveryAddress;
    private final Long totalPrice;
    private final LocalDateTime createdAt;
    private final String status;
    private final String adminName;
    private final String adminEmail;
    private final String adminRole;

    public OrderGetResponse(String orderNumber, String customerName, String customerEmail, List<OrderProductResponse> products, String receiverName, String receiverPhone, String deliveryAddress, Long totalPrice, LocalDateTime createdAt, String status, String adminName, String adminEmail, String adminRole) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.products = products;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminRole = adminRole;
    }

    public static OrderGetResponse from(Order order, List<OrderProductResponse> products){

        // 관리자 정보는 없다고 가정하려고 null로 할당
        String adminName = null;
        String adminEmail = null;
        String adminRole = null;

        // 주문에 관리자 정보가 있으면 CS주문으로 관리자 정보를 조회해서 다시 재할당
        // 주문에 관리자 정보가 없으면 관리자 정보를 조회하지않고 null 유지
        if (order.getAdmin() != null){
            Admin admin = order.getAdmin();
            adminName = admin.getName();
            adminEmail = admin.getEmail();
            adminRole = admin.getRole().getDescription();
        }
        return new OrderGetResponse(
                order.getOrderNumber(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                products,
                order.getReceiverName(),
                order.getReceiverPhone(),
                order.getDeliveryAddress(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus().getDescription(),
                adminName,
                adminEmail,
                adminRole
        );
    }
}
