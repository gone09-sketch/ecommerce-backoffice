package com.ecommercebackoffice.order.controller;

import com.ecommercebackoffice.common.CommonResponse;
import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.order.dto.*;
import com.ecommercebackoffice.order.enums.OrderStatus;
import com.ecommercebackoffice.order.service.OrderService;
import com.ecommercebackoffice.session.SessionAdmin;
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
    public ResponseEntity<CommonResponse<OrderCreateResponse>> save(
            @Valid @RequestBody OrderCreateRequest request,
            HttpSession session
    ) {
        SessionAdmin sessionAdmin = (SessionAdmin) session.getAttribute("loginAdmin");
        Long adminId = sessionAdmin.getId();

        OrderCreateResponse result = orderService.save(request, adminId);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.created("주문 생성 성공",result));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderGetResponse>> getOne(
            @PathVariable Long orderId
    ) {
        OrderGetResponse result = orderService.findOne(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("주문 상세 조회 성공",result));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<OrderListResponse>>> getPage(
            @ModelAttribute OrderListRequest request
    ) {
        Page<OrderListResponse> page = orderService.findAll(request);

        PageResponse<OrderListResponse> pageResponse = new PageResponse<>(page);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("주문 리스트 조회 성공",pageResponse));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderUpdateResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatus orderStatus
    ) {
        OrderUpdateResponse result = orderService.update(orderId, orderStatus);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("주문 상태 수정 성공",result));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<CommonResponse<OrderCancelResponse>> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelRequest request
    ) {
        OrderCancelResponse result = orderService.cancel(orderId, request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("주문 취소 성공",result));
    }
}
