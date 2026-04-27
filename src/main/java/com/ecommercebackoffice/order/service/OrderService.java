package com.ecommercebackoffice.order.service;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.exception.OrderNotFoundException;
import com.ecommercebackoffice.exception.ProductNotFoundException;
import com.ecommercebackoffice.order.dto.*;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.entity.OrderProduct;
import com.ecommercebackoffice.order.enums.OrderStatus;
import com.ecommercebackoffice.order.repository.OrderProductRepository;
import com.ecommercebackoffice.order.repository.OrderRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import com.ecommercebackoffice.session.SessionAdminDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    public OrderCreateResponse save(@Valid OrderCreateRequest request, HttpSession httpSession) {

        SessionAdminDto loginAdmin = (SessionAdminDto) httpSession.getAttribute("loginAdmin");
        Long adminId = loginAdmin.getId();

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지않는 관리자입니다.")
        );
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new IllegalStateException("존재하지않는 고객입니다.")
        );
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("존재하지않는 상품입니다.")
        );

        if (product.getStatus().equals("단종"))
            throw new IllegalStateException("단종 상품은 주문할 수 없습니다.");
        if (product.getStatus().equals("품절"))
            throw new IllegalStateException("품절 상품은 주문할 수 없습니다.");
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalStateException("재고가 부족합니다");
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

        product.updateStock(request.getQuantity());
        Order savedOrder = orderRepository.save(order);
        return new OrderCreateResponse(
                savedOrder.getId(),
                savedOrder.getCreatedAt(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus().getDescription(),
                savedOrder.getQuantity(),
                savedOrder.getOrderPrice(),
                savedOrder.getTotalPrice(),
                savedOrder.getAdmin().getId()
        );
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderGetResponse findOne(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(orderId);

        if (orderProducts.isEmpty()){
            throw new IllegalStateException("주문 상품 정보가 없습니다.");
        }

        List<OrderProductResponse> products = orderProducts.stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());

        return OrderGetResponse.from(order,products);
    }

    // 주문 페이지 조회
    @Transactional(readOnly = true)
    public Page<OrderListResponse> findAll(OrderListRequest request) {

        // 정렬 기준 기본값 createdAt, 정렬 기준이 적혀있지 않으면 기본값으로 반환
       if (request.getSortBy() ==null|| request.getSortBy().isBlank()){
           request.setSortBy("createdAt");
       }

       Pageable pageable = request.toPageable();

       // 반환타입이 Page<Order>여서 Response로 바꾸기 위해 메서드를 넣음
        return orderRepository.findAll(pageable)
                .map(order -> toOrderListResponse(order));
    }

    // Order를 OrderListResponse로 바꾸는 메서드
    private OrderListResponse toOrderListResponse(Order order){

        // 해당 주문의 상품 목록 조회
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(order.getId());

        if (orderProducts.isEmpty()){
            throw new IllegalStateException("주문 상품 정보가 없습니다.");
        }
        return OrderListResponse.from(order,orderProducts);
    }

    @Transactional
    public OrderUpdateResponse update(Long orderId, OrderStatus orderStatus) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );

        // 현재 주문 상태가 배송완료면 예외
        if (order.getStatus().equals(OrderStatus.DELIVERED))
            throw new IllegalStateException("배송완료 상태의 주문은 변경할 수 없습니다.");

        // && 앞은 현재 주문 상태
        // && 뒤는 요청 주문 상태
        if(order.getStatus().equals(OrderStatus.READY) && orderStatus.equals(OrderStatus.SHIPPING)){
            order.updateStatus(OrderStatus.SHIPPING);
        } else if (order.getStatus().equals(OrderStatus.SHIPPING) && orderStatus.equals(OrderStatus.DELIVERED)){
            order.updateStatus(OrderStatus.DELIVERED);
        } else {
            throw new IllegalStateException("잘못된 요청입니다.");
        }

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(order.getId());

        if (orderProducts.isEmpty()){
            throw new IllegalStateException("주문 상품 정보가 없습니다.");
        }

        // 응답으로 엔티티가 아닌 Response로 내보내기 위해 주문 상품 하나씩 Response로 바꿔 다시 리스트로 만들기
        List<OrderProductResponse> products = orderProducts.stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());

        return OrderUpdateResponse.from(order,products);
    }

    @Transactional
    public OrderCancelResponse cancel(Long orderId, OrderCancelRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(order.getId());

        if (orderProducts.isEmpty()){
            throw new IllegalStateException("주문 상품 정보가 없습니다.");
        }

        if (!order.getStatus().equals(OrderStatus.READY)){
            throw new IllegalStateException("준비중 상태의 주문만 취소할 수 있습니다.");
        }

        order.cancel(request.getCancelReason());

        for (OrderProduct orderProduct : orderProducts){
            Product product = productRepository.findById(orderProduct.getProductId()).orElseThrow(
                    () -> new ProductNotFoundException("존재하지 않는 상품입니다.")
            );
            product.updateStock(orderProduct.getQuantity());
        }
        return OrderCancelResponse.from(order);
    }
}

