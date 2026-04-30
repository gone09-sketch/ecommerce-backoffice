package com.ecommercebackoffice.order.repository;

import com.ecommercebackoffice.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrder_Id(Long orderId);
}
