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

    /**
     * 주문 상품(OrderProduct) 데이터를 기반으로 여러 고객의 주문 통계 정보를 조회한다.
     *
     * <p>각 고객별로 다음 정보를 집계한다.</p>
     * <ul>
     *     <li>총 주문 수 (COUNT DISTINCT order.id)</li>
     *     <li>총 구매 금액 (SUM totalPrice)</li>
     * </ul>
     *
     * <p>특정 상태(excludedStatus)의 주문은 통계에서 제외된다.</p>
     *
     * @param customerIds 조회 대상 고객 ID 목록
     * @param excludedStatus 제외할 주문 상태 (예: CANCELED)
     * @return 고객 ID별 주문 통계 리스트
     */
    @Query("""
    SELECT new com.ecommercebackoffice.customer.dto.CustomerOrderStatus(
        op.order.customer.id,
        COUNT(DISTINCT op.order.id),
        COALESCE(SUM(op.totalPrice), 0L)
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

    /**
     * 주문 상품(OrderProduct) 데이터를 기반으로 특정 고객 1명의 주문 통계 정보를 조회한다.
     *
     * <p>조회되는 정보:</p>
     * <ul>
     *     <li>총 주문 수</li>
     *     <li>총 구매 금액</li>
     * </ul>
     *
     * <p>해당 고객의 주문이 없을 경우 Optional.empty()를 반환한다.</p>
     * <p>특정 상태(excludedStatus)의 주문은 통계에서 제외된다.</p>
     *
     * @param customerId 조회 대상 고객 ID
     * @param excludedStatus 제외할 주문 상태
     * @return 고객 주문 통계 (없을 경우 Optional.empty)
     */
    @Query("""
    SELECT new com.ecommercebackoffice.customer.dto.CustomerOrderStatus(
        op.order.customer.id,
        COUNT(DISTINCT op.order.id),
        COALESCE(SUM(op.totalPrice), 0L)
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
