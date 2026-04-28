package com.ecommercebackoffice.product.entity;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.config.BaseEntity;
import com.ecommercebackoffice.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import static com.ecommercebackoffice.product.enums.ProductStatus.*;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
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

    public void updateStock(int stock) {
        this.stock = stock;

        if (!this.status.equals("단종")) {
            if (this.stock >= 1) {
                this.status = ON_SALE;
            } else {
                this.status = SOLD_OUT;
            }
        }
    }

    public void descStock(int quantity) {
        this.stock -= quantity;

        if (stock <= 0) {
            stock = 0;
            this.status = SOLD_OUT;
        }
    }

    public void revertStock(int quantity) {
        this.stock += quantity;

        if (this.status != DISCONTINUED) {
            if (stock > 0) {
                this.status = ON_SALE;
            }
        }
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }
}
