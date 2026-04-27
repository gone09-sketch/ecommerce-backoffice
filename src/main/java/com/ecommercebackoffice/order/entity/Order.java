package com.ecommercebackoffice.order.entity;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long orderPrice;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String receiverPhone;

    @Column(nullable = false)
    private String deliveryAddress;

    @CreatedDate
    @Column(nullable = false ,updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime canceledAt;

    private String cancelReason;

    public Order(Admin admin
            ,Customer customer
            ,Integer quantity
            ,Long orderPrice
            ,String receiverName
            , String receiverPhone
            ,String deliveryAddress)
    {
        this.admin = admin;
        this.customer = customer;
        this.orderNumber = createOrderNumber(customer.getId());
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.totalPrice = orderPrice * quantity;
        this.status = OrderStatus.READY;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.deliveryAddress = deliveryAddress;
    }

    // 주문 번호 만들기 로직
    private String createOrderNumber(Long customerId){
        return customerId
                + "-"
                + LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMdd"));
    }

    // 주문 상태 변환 메서드
    public void updateStatus(OrderStatus status){
        this.status = status;
    }

    public void cancel(String cancelReason) {
        this.status = OrderStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
        this.cancelReason = cancelReason;
    }
}
