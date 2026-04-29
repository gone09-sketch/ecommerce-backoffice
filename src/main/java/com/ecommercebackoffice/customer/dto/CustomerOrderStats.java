package com.ecommercebackoffice.customer.dto;

import lombok.Getter;

@Getter
public class CustomerOrderStats {

    private final Long customerId;
    private final Long totalOrderCount;
    private final Long totalPurchaseAmount;

    public CustomerOrderStats(Long customerId, Long totalOrderCount, Number totalPurchaseAmount) {
        this.customerId = customerId;
        this.totalOrderCount = totalOrderCount;
        this.totalPurchaseAmount = totalPurchaseAmount.longValue();
    }
}
