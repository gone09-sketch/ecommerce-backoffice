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
import com.ecommercebackoffice.order.service.query.OrderQueryService;
import com.ecommercebackoffice.order.service.validator.OrderSortValidator;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.enums.ProductStatus;
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
    private final OrderSortValidator orderSortValidator;
    private final OrderQueryService orderQueryService;

    // 주문 생성
    @Transactional
    public OrderCreateResponse save(OrderCreateRequest request, Long adminId) {

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException()
        );
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new CustomerNotFoundException()
        );
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ProductNotFoundException()
        );

        product.validateOrderable(request.getQuantity());

        Order order = new Order(
                admin,
                customer,
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
        OrderProduct savedOrderProduct = orderProductRepository.save(orderProduct);

        return OrderCreateResponse.from(savedOrder,orderProduct);
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderGetResponse findOne(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException()
        );

        List<OrderProduct> orderProducts = orderProductRepository.findByOrder_Id(orderId);

        if (orderProducts.isEmpty()) {
            throw new OrderProductNotFoundException();
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

        // 정렬 검증
        orderSortValidator.validateSortBy(request.getSortBy());
        orderSortValidator.validateSortOrder(request.getSortOrder());

        Pageable pageable = request.toPageable(request.getSortBy());

        // 검색 키워드, 상태 필터
        String keyword = request.getKeyword();
        OrderStatus status = request.getStatus();

        //조회
        Page<Order> orderPage = orderQueryService.searchOrdersBySort(request, keyword, status, pageable);

        // 페이지 범위 검증
        request.validatePageRange(orderPage.getTotalPages());

        // DTO 반환
        return orderPage.map(this::toOrderListResponse);
    }

    // Order를 OrderListResponse로 바꾸는 메서드
    private OrderListResponse toOrderListResponse(Order order) {

        // 해당 주문의 상품 목록 조회
        List<OrderProduct> orderProducts = orderProductRepository.findByOrder_Id(order.getId());

        if (orderProducts.isEmpty()) {
            throw new OrderProductNotFoundException();
        }
        return OrderListResponse.from(order, orderProducts);
    }

    // 주문 상태 변경
    @Transactional
    public OrderUpdateResponse update(Long orderId, OrderStatus orderStatus) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException()
        );

        // 현재 주문 상태가 배송완료면 예외
        if (order.getStatus().equals(OrderStatus.DELIVERED))
            throw new OrderAlreadyCompletedException();

        // && 앞은 현재 주문 상태
        // && 뒤는 요청 주문 상태
        if (order.getStatus().equals(OrderStatus.READY) && orderStatus.equals(OrderStatus.SHIPPING)) {
            order.updateStatus(OrderStatus.SHIPPING);
        } else if (order.getStatus().equals(OrderStatus.SHIPPING) && orderStatus.equals(OrderStatus.DELIVERED)) {
            order.updateStatus(OrderStatus.DELIVERED);
        } else {
            throw new InvalidOrderStatusException();
        }

        List<OrderProduct> orderProducts = orderProductRepository.findByOrder_Id(order.getId());

        if (orderProducts.isEmpty()) {
            throw new OrderProductNotFoundException();
        }

        // 응답으로 엔티티가 아닌 Response로 내보내기 위해 주문 상품 하나씩 Response로 바꿔 다시 리스트로 만들기
        List<OrderProductResponse> products = orderProducts.stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());

        return OrderUpdateResponse.from(order, products);
    }

    // 주문 취소
    @Transactional
    public OrderCancelResponse cancel(Long orderId, OrderCancelRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException()
        );

        List<OrderProduct> orderProducts = orderProductRepository.findByOrder_Id(order.getId());

        if (!order.getStatus().equals(OrderStatus.READY)) {
            throw new OrderCancellationNotAllowedException();
        }

        order.cancel(request.getCancelReason());

        // 주문 취소 후 재고 원복 메서드
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productRepository.findById(orderProduct.getProductId()).orElseThrow(
                    () -> new ProductNotFoundException()
            );
            product.revertStock(orderProduct.getQuantity());
        }
        return OrderCancelResponse.from(order);
    }
}

