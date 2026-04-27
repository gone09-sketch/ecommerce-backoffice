package com.ecommercebackoffice.product.entity;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.config.BaseEntity;
import com.ecommercebackoffice.product.enums.ProductEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import static com.ecommercebackoffice.product.enums.ProductEnum.ON_SALE;
import static com.ecommercebackoffice.product.enums.ProductEnum.SOLD_OUT;

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
    private ProductEnum status = ON_SALE;

    /* 현재 login을 통해 SessionUser 객체에 Product 등록을 요청한 Admin의 Id를 저장하는 기능이 없어,
     Product 등록 시에 어떤 Admin객체를 참조해야 하는지를 지정할 수 없는 상태이기에 일단 연관관계 매핑부분은 다 빼 놓은 상황입니다.*/
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "admin_id", nullable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    private Admin admin;

    public Product(String name, String category, Long price, int stock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
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
                this.status = ON_SALE;
            } else {
                this.status = SOLD_OUT;
            }
        }
    }

    public void updateStatus(ProductEnum status) {
        this.status = status;
    }
}
