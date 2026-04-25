package com.ecommercebackoffice.customer.controller;

import com.ecommercebackoffice.customer.dto.*;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import com.ecommercebackoffice.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    // 고객 리스트 조회
    @GetMapping
    public ResponseEntity<CustomerGetListResponse> getCustomers() {

        CustomerGetListResponse customersList = customerService.getCustomersList();

        return ResponseEntity.status(HttpStatus.OK).body(customersList);
    }

    //고객 단 건 조회
    @GetMapping("/{customers}")
    public ResponseEntity<CustomerGetListResponse.CustomerGetDto> getOneCustomer(@PathVariable Long customerId) {

        CustomerGetListResponse.CustomerGetDto result = customerService.getByCustomerId(customerId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //고객 수정
    @PatchMapping("/{customers}")
    public ResponseEntity<CustomerUpdateResponse> update(
            @PathVariable Long customerId, @RequestBody @Valid CustomerUpdateRequest request
    ) {
        CustomerUpdateResponse result = customerService.update(customerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    // 고객 상태 수정
//    @PatchMapping("/{customers}")
//    public ResponseEntity<CustomerStatusUpdateRequest> statusUpdate(
//            @PathVariable Long customerId, @RequestBody CustomerStatusUpdateRequest request) {
//
//        CustomerStatusUpdateResponse reult =customerService.statusUpdate(customerId,request);
//
//    }

//    //고객 삭제
//    @DeleteMapping("/{customers}")
//    public


}
