package com.ecommercebackoffice.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long orderPrice;

    @Column(nullable = false)
    private Long totalPrice;

    public OrderProduct(Order order, Long productId, String productName, Integer quantity, Long orderPrice){
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.totalPrice = orderPrice * quantity;
    }
}
