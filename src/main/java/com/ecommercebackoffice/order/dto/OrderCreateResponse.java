package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.entity.OrderProduct;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderCreateResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final String orderNumber;
    private final String status;
    private final List<OrderProductResponse> products; // 주문에 포함된 상품들을 보여주기 위한 필드
    private final Integer totalQuantity;
    private final Long totalPrice;
    private final Long adminId;

    public OrderCreateResponse(Long id
            , LocalDateTime createdAt
            , String orderNumber
            , String status
            , List<OrderProductResponse> products
            , Integer totalQuantity
            , Long totalPrice
            , Long adminId
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderNumber = orderNumber;
        this.status = status;
        this.products = products;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.adminId = adminId;
    }

    public static OrderCreateResponse from(Order order, List<OrderProduct> orderProducts) {
        List<OrderProductResponse> products = orderProducts.stream()
                .map(OrderProductResponse::from)
                .toList();

        int totalQuantity = orderProducts.stream()
                .mapToInt(OrderProduct::getQuantity)
                .sum();

        long totalPrice = orderProducts.stream()
                .mapToLong(OrderProduct::getTotalPrice)
                .sum();

        return new OrderCreateResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getOrderNumber(),
                order.getStatus().getDescription(),
                products,
                totalQuantity,
                totalPrice,
                order.getAdmin().getId()
        );
    }
}
