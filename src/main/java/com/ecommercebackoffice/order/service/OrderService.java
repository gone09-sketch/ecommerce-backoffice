package com.ecommercebackoffice.order.service;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.exception.AdminNotFoundException;
import com.ecommercebackoffice.exception.OrderNotFoundException;
import com.ecommercebackoffice.exception.ProductNotFoundException;
import com.ecommercebackoffice.order.dto.OrderCreateRequest;
import com.ecommercebackoffice.order.dto.OrderCreateResponse;
import com.ecommercebackoffice.order.dto.OrderGetResponse;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.entity.OrderProduct;
import com.ecommercebackoffice.order.repository.OrderProductRepository;
import com.ecommercebackoffice.order.repository.OrderRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public OrderCreateResponse save(@Valid OrderCreateRequest request, Long adminId) {
        if (request.getQuantity() < 1) {
            throw new IllegalStateException("수량은 1 이상이어야 합니다.");
        }
        adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지않는 관리자입니다.")
        );
        customerRepository.findById(request.getCustomerId()).orElseThrow(
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
                adminId,
                request.getCustomerId(),
                request.getQuantity(),
                orderPrice,
                request.getReceiverName(),
                request.getReceiverPhone(),
                request.getDeliveryAddress()
        );
//        product.updateStock(request.getQuantity());  상품쪽에서 재고차감 메서드 부탁해야함
        Order savedOrder = orderRepository.save(order);
        return new OrderCreateResponse(
                savedOrder.getId(),
                savedOrder.getCreatedAt(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus().getDescription(),
                savedOrder.getQuantity(),
                savedOrder.getOrderPrice(),
                savedOrder.getTotalPrice(),
                savedOrder.getAdminId()
        );
    }

    public OrderGetResponse findOne(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("존재하지 않는 주문입니다.")
        );
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(orderId);
        if(orderProducts.isEmpty()){
            throw new IllegalStateException("주문 상품 정보가 없습니다.");
            }
        OrderProduct orderProduct = orderProducts.get(0);
        Customer customer = customerRepository.findById(order.getCustomerId()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 고객입니다.")
        );

        // 관리자 정보는 없다고 가정하려고 null로 할당
        String adminName = null;
        String adminEmail = null;
        String adminRole = null;

        // 주문에 관리자 id가 있으면 CS주문으로 관리자 정보를 조회해서 다시 재할당
        // 주문에 관리자 id가 없으면 관리자 정보를 조회하지않고 null 유지
        if (order.getAdminId() != null){
            Admin admin = adminRepository.findById(order.getAdminId()).orElseThrow(
                    () -> new AdminNotFoundException("존재하지 않는 관리자입니다.")
            );
            adminName = admin.getName();
            adminEmail = admin.getEmail();
            adminRole = admin.getRole();
        }

        return new OrderGetResponse(
                order.getOrderNumber(),
                customer.getName(),
                customer.getEmail(),
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus().getDescription(),
                adminName,
                adminEmail,
                adminRole
        );
    }

}

