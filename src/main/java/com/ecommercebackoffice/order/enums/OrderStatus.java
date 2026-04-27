package com.ecommercebackoffice.order.enums;

public enum OrderStatus {
    READY("준비중"),
    SHIPPING("배송중"),
    DELIVERED("배송완료"),
    CANCELED("주문취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
