package com.ecommercebackoffice.order.repository;

import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

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

