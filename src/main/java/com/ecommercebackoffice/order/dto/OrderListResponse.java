package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.entity.OrderProduct;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderListResponse {
    private final Long id;
    private final String orderNumber;
    private final String customerName;
    private final String productName;
    private final Integer quantity;
    private final Long totalPrice;
    private final LocalDateTime createdAt;
    private final String status;
    private final String adminName;

    public OrderListResponse(Long id, String orderNumber, String customerName, String productName, Integer quantity, Long totalPrice, LocalDateTime createdAt, String status, String adminName) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.adminName = adminName;
    }

    public static OrderListResponse from(Order order, List<OrderProduct> orderProducts){

        // 주문 상품이 1개 or 여러개 일 때 상품명을 보여주는 방법
        String productName;
        if (orderProducts.size() ==1){
            productName = orderProducts.get(0).getProductName();
        } else {
            productName = orderProducts.get(0).getProductName()
                    + " 외 "
                    + (orderProducts.size()-1)
                    + "건";
        }

        // 주문 상품들의 수량 및 총 금액을 합계로 나타내기 (상세로 조회하면 각 상품당 수량과 총 금액 확인 가능)
        int  quantity = 0;
        long totalPrice = 0L;

        for (OrderProduct orderProduct : orderProducts){
            quantity += orderProduct.getQuantity();
            totalPrice += orderProduct.getTotalPrice();
        }

        // 관리자가 등록한 주문은 관리자명이 출력되고, 고객이 등록한 주문은 관리자명을 null로 들어감
        String adminName = null;
        if (order.getAdmin() !=null){
            adminName = order.getAdmin().getName();
        }

        return new OrderListResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomer().getName(),
                productName,
                quantity,
                totalPrice,
                order.getCreatedAt(),
                order.getStatus().getDescription(),
                adminName
        );
    }
}
