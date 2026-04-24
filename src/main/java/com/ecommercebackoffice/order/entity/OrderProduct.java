package com.ecommercebackoffice.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orderproducts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
}
