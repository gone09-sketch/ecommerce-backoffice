package com.ecommercebackoffice.order.repository;

import com.ecommercebackoffice.customer.dto.CustomerOrderStats;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 기본값으로 설정된 조회
    @Query("""
            SELECT o 
            FROM Order o          
            WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR o.customer.name LIKE %:keyword%)        
            AND (:status IS NULL OR o.status = :status)                                     
            """)
    Page<Order> searchOrders(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    // 키워드 수량이고 내림차순으로 조회
    @Query("""
            SELECT o
            FROM Order o
            JOIN OrderProduct op ON op.order = o
            WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR o.customer.name LIKE %:keyword%)
              AND (:status IS NULL OR o.status = :status)
            GROUP BY o
            ORDER BY SUM(op.quantity) DESC
            """)
    Page<Order> searchOrdersOrderByQuantityDesc(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    // 키워드 수량이고 오름차순으로 조회
    @Query("""
            SELECT o
            FROM Order o
            JOIN OrderProduct op ON op.order = o
            WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR o.customer.name LIKE %:keyword%)
              AND (:status IS NULL OR o.status = :status)
            GROUP BY o
            ORDER BY SUM(op.quantity) ASC
            """)
    Page<Order> searchOrdersOrderByQuantityAsc(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    // 키워드 총금액이고 내림차순으로 조회
    @Query("""
            SELECT o
            FROM Order o
            JOIN OrderProduct op ON op.order = o
            WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR o.customer.name LIKE %:keyword%)
              AND (:status IS NULL OR o.status = :status)
            GROUP BY o
            ORDER BY SUM(op.totalPrice) DESC
            """)
    Page<Order> searchOrdersOrderByTotalPriceDesc(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    // 키워드 총금액이고 오름차순으로 조회
    @Query("""
            SELECT o
            FROM Order o
            JOIN OrderProduct op ON op.order = o
            WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR o.customer.name LIKE %:keyword%)
              AND (:status IS NULL OR o.status = :status)
            GROUP BY o
            ORDER BY SUM(op.totalPrice) ASC
            """)
    Page<Order> searchOrdersOrderByTotalPriceAsc(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

}

