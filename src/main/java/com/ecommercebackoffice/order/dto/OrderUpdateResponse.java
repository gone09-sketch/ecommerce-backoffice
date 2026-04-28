package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.order.entity.Order;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderUpdateResponse {
    private final String orderNumber;
    private final String customerName;
    private final List<OrderProductResponse> products;
    private final String status;

    public OrderUpdateResponse(String orderNumber, String customerName, List<OrderProductResponse> products, String status) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.products = products;
        this.status = status;
    }

    public static OrderUpdateResponse from(Order order, List<OrderProductResponse> products) {
        return new OrderUpdateResponse(
                order.getOrderNumber(),
                order.getCustomer().getName(),
                products,
                order.getStatus().getDescription()
        );
    }
}
