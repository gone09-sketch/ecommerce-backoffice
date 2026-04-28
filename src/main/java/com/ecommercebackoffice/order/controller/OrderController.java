package com.ecommercebackoffice.order.controller;

import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.order.dto.*;

import com.ecommercebackoffice.order.enums.OrderStatus;
import com.ecommercebackoffice.order.service.OrderService;
import com.ecommercebackoffice.session.SessionAdminDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> save(
            @Valid @RequestBody OrderCreateRequest request,
            HttpSession session) {
        SessionAdminDto loginAdmin = (SessionAdminDto) session.getAttribute("LOGIN_ADMIN");

        if (loginAdmin == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Long adminId = loginAdmin.getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(request, adminId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderGetResponse> getOne(
            @PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOne(orderId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderListResponse>> getPage(@ModelAttribute OrderListRequest request) {
        Page<OrderListResponse> page = orderService.findAll(request);
        return ResponseEntity.status(HttpStatus.OK).body(new PageResponse<>(page));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderUpdateResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatus orderStatus) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.update(orderId, orderStatus));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderCancelResponse> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancel(orderId, request));
    }
}
