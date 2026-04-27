package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.order.entity.Order;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderUpdateResponse {
    private final String orderNumber;
    private final String receiverName;
    private final List<OrderProductResponse> products;
    private final String status;

    public OrderUpdateResponse(String orderNumber, String receiverName, List<OrderProductResponse> products, String status) {
        this.orderNumber = orderNumber;
        this.receiverName = receiverName;
        this.products = products;
        this.status = status;
    }

    public static OrderUpdateResponse from(Order order, List<OrderProductResponse> products){
        return new OrderUpdateResponse(
                order.getOrderNumber(),
                order.getReceiverName(),
                products,
                order.getStatus().getDescription()
        );
    }
}
