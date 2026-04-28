package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.order.entity.OrderProduct;
import lombok.Getter;

@Getter
public class OrderProductResponse {

    private final String productName;
    private final Integer quantity;
    private final Long orderPrice;
    private final Long totalPrice;

    public OrderProductResponse(String productName, Integer quantity, Long orderPrice, Long totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.totalPrice = totalPrice;
    }

    public static OrderProductResponse from(OrderProduct orderProduct) {
        return new OrderProductResponse(
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getOrderPrice(),
                orderProduct.getTotalPrice()
        );
    }
}
