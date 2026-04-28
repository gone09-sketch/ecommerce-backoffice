package com.ecommercebackoffice.order.dto;

import com.ecommercebackoffice.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderCancelResponse {

    private final String orderNumber;
    private final String status;
    private final String cancelReason;
    private final LocalDateTime canceledAt;

    public OrderCancelResponse(String orderNumber, String status, String cancelReason, LocalDateTime canceledAt) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.cancelReason = cancelReason;
        this.canceledAt = canceledAt;
    }

    public static OrderCancelResponse from(Order order) {
        return new OrderCancelResponse(
                order.getOrderNumber(),
                order.getStatus().getDescription(),
                order.getCancelReason(),
                order.getCanceledAt()
        );
    }
}
