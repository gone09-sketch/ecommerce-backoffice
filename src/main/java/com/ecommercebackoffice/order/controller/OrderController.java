package com.ecommercebackoffice.order.controller;

import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.order.dto.*;

import com.ecommercebackoffice.order.enums.OrderStatus;
import com.ecommercebackoffice.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderCreateResponse> save(
            @Valid @RequestBody OrderCreateRequest request,
            HttpSession httpSession){

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(request,httpSession));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderGetResponse> getOne(
            @PathVariable Long orderId){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findOne(orderId));
    }

    @GetMapping("/orders")
    public ResponseEntity<PageResponse<OrderListResponse>> getPage(@ModelAttribute OrderListRequest request){
        Page<OrderListResponse> page = orderService.findAll(request);
        return ResponseEntity.status(HttpStatus.OK).body(new PageResponse<>(page));
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<OrderUpdateResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatus orderStatus){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.update(orderId,orderStatus));
    }

    @PatchMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderCancelResponse> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancel(orderId,request));
    }
}
