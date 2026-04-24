package com.ecommercebackoffice.order.service;

import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.order.dto.OrderCreateRequest;
import com.ecommercebackoffice.order.dto.OrderCreateResponse;
import com.ecommercebackoffice.order.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public OrderCreateResponse save(@Valid OrderCreateRequest request, Long adminId) {
        if (request.getQuantity()<1){
            throw new IllegalStateException("재고가 부족합니다.");
        }

    }
}
