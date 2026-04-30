package com.ecommercebackoffice.order.service.query;

import com.ecommercebackoffice.order.dto.OrderListRequest;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.enums.OrderStatus;
import com.ecommercebackoffice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    // 정렬 조건에 따라 어떤 repository 메서드 호출할지 결정하는 메서드
    public Page<Order> searchOrdersBySort(
            OrderListRequest request,
            String keyword,
            OrderStatus status,
            Pageable pageable
    ) {
        // 정렬 기준 및 순서 변수로 선언
        // equalsIgnoreCase는 대소문자 구분없이 비교함
        boolean isQuantitySort = "quantity".equals(request.getSortBy());
        boolean isTotalPriceSort = "totalPrice".equals(request.getSortBy());
        boolean isDescSort = "desc".equalsIgnoreCase(request.getSortOrder());
        boolean isAscSort = "asc".equalsIgnoreCase(request.getSortOrder());

        if (isQuantitySort && isDescSort) {
            return orderRepository.searchOrdersOrderByQuantityDesc(keyword, status, pageable);
        }

        if (isQuantitySort && isAscSort) {
            return orderRepository.searchOrdersOrderByQuantityAsc(keyword, status, pageable);
        }

        if (isTotalPriceSort && isDescSort) {
            return orderRepository.searchOrdersOrderByTotalPriceDesc(keyword, status, pageable);
        }

        if (isTotalPriceSort && isAscSort) {
            return orderRepository.searchOrdersOrderByTotalPriceAsc(keyword, status, pageable);
        }

        return orderRepository.searchOrders(keyword, status, pageable);
    }
}
