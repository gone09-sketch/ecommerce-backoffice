package com.ecommercebackoffice.order.service;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.exception.*;
import com.ecommercebackoffice.order.dto.*;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.entity.OrderProduct;
import com.ecommercebackoffice.order.enums.OrderStatus;
import com.ecommercebackoffice.order.repository.OrderProductRepository;
import com.ecommercebackoffice.order.repository.OrderRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    // 주문 생성
    @Transactional
    public OrderCreateResponse save(OrderCreateRequest request, Long adminId) {

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException("존재하지 않는 관리자입니다.")
        );
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 고객입니다.")     // 예외 꼭 바꾸기
        );
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품입니다.")
        );

        if (product.getStatus().equals("단종"))
            throw new BadRequestException("단종 상품은 주문할 수 없습니다.");
        if (product.getStatus().equals("품절"))
            throw new BadRequestException("품절 상품은 주문할 수 없습니다.");
        if (product.getStock() < request.getQuantity()) {
            throw new BadRequestException("재고가 부족합니다");
        }
        Long orderPrice = product.getPrice();
        Order order = new Order(
                admin,
                customer,
                request.getQuantity(),
                orderPrice,
                request.getReceiverName(),
                request.getReceiverPhone(),
                request.getDeliveryAddress()
        );

        // 주문 차감 메서드
        product.descStock(request.getQuantity());

        Order savedOrder = orderRepository.save(order);

        // orderProduct 저장
        OrderProduct orderProduct = new OrderProduct(
                savedOrder,
                product.getId(),
                product.getName(),
                request.getQuantity(),
                product.getPrice()
        );
        orderProductRepository.save(orderProduct);

        return OrderCreateResponse.from(savedOrder);
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderGetResponse findOne(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(orderId);

        if (orderProducts.isEmpty()) {
            throw new OrderProductNotFoundException("주문 상품 정보가 없습니다.");
        }

        List<OrderProductResponse> products = orderProducts.stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());

        return OrderGetResponse.from(order, products);
    }

    // 주문 페이지 조회
    @Transactional(readOnly = true)
    public Page<OrderListResponse> findAll(OrderListRequest request) {

        // 정렬 기준 기본값 createdAt, 정렬 기준이 적혀있지 않으면 기본값으로 반환
        if (request.getSortBy() == null || request.getSortBy().isBlank()) {
            request.setSortBy("createdAt");
        }

        validateSortBy(request.getSortBy());
        validateSortOrder(request.getSortOrder());

        Pageable pageable = request.toPageable();

        // 검색 키워드, 상태 필터
        String keyword = request.getKeyword();
        OrderStatus status = request.getStatus();

        // 정렬 조건에 맞는 조회를 실행하고 만들어진 결과인 Page<Order>를 Page<OrderListResponse>로 바꾸는 메서드 실행
        return searchOrdersBySort(request, keyword, status, pageable)
                .map(order -> toOrderListResponse(order));
    }

    // Order를 OrderListResponse로 바꾸는 메서드
    private OrderListResponse toOrderListResponse(Order order) {

        // 해당 주문의 상품 목록 조회
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(order.getId());

        if (orderProducts.isEmpty()) {
            throw new OrderProductNotFoundException("주문 상품 정보가 없습니다.");
        }
        return OrderListResponse.from(order, orderProducts);
    }

    // 정렬 조건에 따라 어떤 repository 메서드 호출할지 결정하는 메서드
    private Page<Order> searchOrdersBySort(
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

    // 정렬 기준이 허용된 값인지 검사
    private void validateSortBy(String sortBy) {
        boolean isCreatedAtSort = "createdAt".equals(sortBy);
        boolean isQuantitySort = "quantity".equals(sortBy);
        boolean isTotalPriceSort = "totalPrice".equals(sortBy);

        if (!isCreatedAtSort && !isQuantitySort && !isTotalPriceSort) {
            throw new BadRequestException("지원하지 않는 정렬 기준입니다.");
        }
    }

    // 정렬 순서가 허용된 값인지 검사
    private void validateSortOrder(String sortOrder) {
        boolean isAscSort = "asc".equalsIgnoreCase(sortOrder);
        boolean isDescSort = "desc".equalsIgnoreCase(sortOrder);

        if (!isAscSort && !isDescSort) {
            throw new BadRequestException("지원하지 않는 정렬 순서입니다.");
        }
    }

    @Transactional
    public OrderUpdateResponse update(Long orderId, OrderStatus orderStatus) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );

        // 현재 주문 상태가 배송완료면 예외
        if (order.getStatus().equals(OrderStatus.DELIVERED))
            throw new InvalidOrderStatusException("배송완료 상태의 주문은 변경할 수 없습니다.");

        // && 앞은 현재 주문 상태
        // && 뒤는 요청 주문 상태
        if (order.getStatus().equals(OrderStatus.READY) && orderStatus.equals(OrderStatus.SHIPPING)) {
            order.updateStatus(OrderStatus.SHIPPING);
        } else if (order.getStatus().equals(OrderStatus.SHIPPING) && orderStatus.equals(OrderStatus.DELIVERED)) {
            order.updateStatus(OrderStatus.DELIVERED);
        } else {
            throw new InvalidOrderStatusException("주문 상태는 준비중 -> 배송중 -> 배송완료 순서로만 변경할 수 있습니다.");
        }

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(order.getId());

        if (orderProducts.isEmpty()) {
            throw new OrderProductNotFoundException("주문 상품 정보가 없습니다.");
        }

        // 응답으로 엔티티가 아닌 Response로 내보내기 위해 주문 상품 하나씩 Response로 바꿔 다시 리스트로 만들기
        List<OrderProductResponse> products = orderProducts.stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());

        return OrderUpdateResponse.from(order, products);
    }

    @Transactional
    public OrderCancelResponse cancel(Long orderId, OrderCancelRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(order.getId());

        if (orderProducts.isEmpty()) {
            throw new OrderProductNotFoundException("주문 상품 정보가 없습니다.");
        }

        if (!order.getStatus().equals(OrderStatus.READY)) {
            throw new InvalidOrderStatusException("준비중 상태의 주문만 취소할 수 있습니다.");
        }

        order.cancel(request.getCancelReason());

        // 주문 취소 후 재고 원복 메서드
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productRepository.findById(orderProduct.getProductId()).orElseThrow(
                    () -> new ProductNotFoundException("존재하지 않는 상품입니다.")
            );
            product.revertStock(orderProduct.getQuantity());
        }
        return OrderCancelResponse.from(order);
    }
}

