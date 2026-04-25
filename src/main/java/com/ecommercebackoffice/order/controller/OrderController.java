package com.ecommercebackoffice.order.controller;

import com.ecommercebackoffice.order.dto.OrderCreateRequest;
import com.ecommercebackoffice.order.dto.OrderCreateResponse;
import com.ecommercebackoffice.order.dto.OrderGetResponse;
import com.ecommercebackoffice.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderCreateResponse> save(
            @Valid @RequestBody OrderCreateRequest request,
            HttpSession session){
//        SessionDto sessionDto = (SessionDto) session.getAttribute("loginAdmin");
//        if (sessionDto==null){
//            throw new IllegalStateException("없는 관리자입니다.");
//        }
//        Long adminId = sessionDto.getId();
        Long adminId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(request,adminId));
    }

//    @GetMapping("/orders")
//    public ResponseEntity<OrderGetResponse> getOne(){
//        return
//    }
}
