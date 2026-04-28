package com.ecommercebackoffice.order.repository;

import com.ecommercebackoffice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

