package com.ecommercebackoffice.order.repository;

import com.ecommercebackoffice.customer.dto.CustomerOrderStatus;
import com.ecommercebackoffice.order.entity.OrderProduct;
import com.ecommercebackoffice.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findAllByOrder_Id(Long orderId);

    @Query("""
        SELECT new com.ecommercebackoffice.customer.dto.CustomerOrderStatus(
            op.order.customer.id,
            COUNT(DISTINCT op.order),
            COALESCE(SUM(op.totalPrice), 0)
        )
        FROM OrderProduct op
        WHERE op.order.customer.id IN :customerIds
          AND op.order.status <> :excludedStatus
        GROUP BY op.order.customer.id
    """)
    List<CustomerOrderStatus> findOrderStatsByCustomerIds(
            @Param("customerIds") List<Long> customerIds,
            @Param("excludedStatus") OrderStatus excludedStatus
    );

    @Query("""
        SELECT new com.ecommercebackoffice.customer.dto.CustomerOrderStatus(
            op.order.customer.id,
            COUNT(DISTINCT op.order),
            COALESCE(SUM(op.totalPrice), 0)
        )
        FROM OrderProduct op
        WHERE op.order.customer.id = :customerId
          AND op.order.status <> :excludedStatus
        GROUP BY op.order.customer.id
    """)
    Optional<CustomerOrderStatus> findOrderStatsByCustomerId(
            @Param("customerId") Long customerId,
            @Param("excludedStatus") OrderStatus excludedStatus
    );
}
