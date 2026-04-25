package com.ecommercebackoffice.order.service;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.order.dto.OrderCreateRequest;
import com.ecommercebackoffice.order.dto.OrderCreateResponse;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.repository.OrderRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderCreateResponse save(@Valid OrderCreateRequest request, Long adminId) {
        if (request.getQuantity()<1){
            throw new IllegalStateException("수량은 1 이상이어야 합니다.");
        }
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("존재하지않는 관리자입니다.")
        );
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new IllegalStateException("존재하지않는 고객입니다.")
        );
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new IllegalStateException("존재하지않는 상품입니다.")
        );
        if (product.getStatus().equals("단종"))
            throw new IllegalStateException("단종 상품은 주문할 수 없습니다.");
        if (product.getStatus().equals("품절"))
            throw new IllegalStateException("품절 상품은 주문할 수 없습니다.");
        if (product.getStock()<request.getQuantity()){
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
//        product.decreaseStock(request.getQuantity());  상품쪽에서 재고차감 메서드 부탁해야함
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
}
