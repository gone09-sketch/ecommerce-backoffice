package com.ecommercebackoffice.customer.controller;

import com.ecommercebackoffice.customer.dto.CustomerGetListResponse;
import com.ecommercebackoffice.customer.dto.CustomerStatusUpdateRequest;
import com.ecommercebackoffice.customer.dto.CustomerStatusUpdateResponse;
import com.ecommercebackoffice.customer.dto.CustomerUpdateRequest;
import com.ecommercebackoffice.customer.dto.CustomerUpdateResponse;
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

    // 고객 단건 조회
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerGetListResponse.CustomerGetResponse> getOneCustomer(
            @PathVariable Long customerId
    ) {
        CustomerGetListResponse.CustomerGetResponse result = customerService.getByCustomerId(customerId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 고객 수정
    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerUpdateResponse> update(
            @PathVariable Long customerId,
            @RequestBody @Valid CustomerUpdateRequest request
    ) {
        CustomerUpdateResponse result = customerService.update(customerId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 고객 상태 수정
    @PatchMapping("/{customerId}/status")
    public ResponseEntity<CustomerStatusUpdateResponse> statusUpdate(
            @PathVariable Long customerId,
            @RequestBody CustomerStatusUpdateRequest request
    ) {
        CustomerStatusUpdateResponse result = customerService.statusUpdate(customerId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 고객 삭제
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {

        customerService.deleteCustomer(customerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
