package com.ecommercebackoffice.product.entity;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.config.BaseEntity;
import com.ecommercebackoffice.product.enums.ProductEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private ProductEnum status = ProductEnum.ON_SALE;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public Product(String name, String category, Long price, int stock,Admin admin) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.admin = admin;
    }

    public void updateInfo(String name, String category, Long price) {
        if(name != null) {
            this.name = name;
        }
        if(category != null) {
            this.category = category;
        }
        if(price != null) {
            this.price = price;
        }
    }

    public void updateStock(int stock) {
        this.stock = stock;

        if(!this.status.equals("단종")) {
            if(this.stock >= 1) {
                this.status = ProductEnum.ON_SALE;
            } else {
                this.status = ProductEnum.SOLD_OUT;
            }
        }
    }
}
