package com.ecommercebackoffice.product.entity;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.common.base.BaseEntity;
import com.ecommercebackoffice.exception.DiscontinuedProductException;
import com.ecommercebackoffice.exception.InsufficientStockException;
import com.ecommercebackoffice.exception.OutOfStockException;
import com.ecommercebackoffice.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Admin admin;

    public Product(String name, String category, Long price, int stock, ProductStatus status, Admin admin) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.admin = admin;
    }

    // 상품 정보 수정 메서드
    public void updateInfo(String name, String category, Long price) {
        if (name != null) {
            this.name = name;
        }
        if (category != null) {
            this.category = category;
        }
        if (price != null) {
            this.price = price;
        }
    }

    // 상품 재고 수정 메서드
    public void updateStock(int stock) {
        this.stock = stock;

        // 해당 상품의 상태가 단종이 아니고, 수정된 재고 상태가 1 이상이라면 ON_SALE, 아니라면 SOLD_OUT으로 변경
        if (this.status != ProductStatus.DISCONTINUED) {
            if (this.stock >= 1) {
                this.status = ProductStatus.ON_SALE;
            } else {
                this.status = ProductStatus.SOLD_OUT;
            }
        }
    }

    // 상품 재고 차감 메서드
    public void descStock(int quantity) {
        this.stock -= quantity;

        // order에서 주문 수량을 매개변수로 전해주면, 재고를 차감하고, 재고가 0이면, 상품 상태를 SOLD_OUT으로 변경
        if (stock <= 0) {
            stock = 0;
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    // 상품 재고 복구 메서드
    public void revertStock(int quantity) {
        this.stock += quantity;

        // order에서 주문 수량을 매개변수로 전해주면, 해당 수량만큼 재고를 복구하고, 상품 상태가 단종이 아니고, 재고가 0보다 크다면 상품 상태를 ON_SALE로 변경
        if (this.status != ProductStatus.DISCONTINUED) {
            if (stock > 0) {
                this.status = ProductStatus.ON_SALE;
            }
        }
    }

    // 상품 재고 및 상태에 따른 주문 가능 여부 판단
    public void validateOrderable(int quantity) {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new DiscontinuedProductException();
        }

        if (this.status == ProductStatus.SOLD_OUT) {
            throw new OutOfStockException();
        }

        if (this.stock < quantity) {
            throw new InsufficientStockException();
        }
    }


    // 상품 상태 수정 메서드
    public void updateStatus(ProductStatus status) {
        this.status = status;
    }
}