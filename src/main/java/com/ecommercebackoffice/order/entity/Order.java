package com.ecommercebackoffice.order.entity;

<<<<<<< HEAD
import com.ecommercebackoffice.order.enums.OrderStatus;
=======
>>>>>>> dev
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
<<<<<<< HEAD

    private Long adminId;

    @NotNull
    private Long customerId;

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

=======
    private Long adminId;
    @NotNull
    private Long customerId;
    @Column(nullable = false)
    private String orderNumber;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Long orderPrice;
    @Column(nullable = false)
    private Long totalPrice;
    @Column(nullable = false)
    private String status;
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
>>>>>>> dev
    private String cancelReason;


    /*
<<<<<<< HEAD
     * 주문번호 생성 메서드
     */
=======
    * 주문번호 생성 메서드
    */
>>>>>>> dev

    private String createOrderNumber(Long customerId){
        return customerId
                + "-"
                + LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMdd"));
    }

    public Order(Long adminId
            ,Long customerId
            ,Integer quantity
            ,Long orderPrice
            ,String receiverName
            , String receiverPhone
            ,String deliveryAddress)
    {
        this.adminId = adminId;
        this.customerId = customerId;
        this.orderNumber = createOrderNumber(customerId);
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.totalPrice = orderPrice * quantity;
<<<<<<< HEAD
        this.status = OrderStatus.READY;
=======
        this.status = "준비중";
>>>>>>> dev
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.deliveryAddress = deliveryAddress;
    }

}
