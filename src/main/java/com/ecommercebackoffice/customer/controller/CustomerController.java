package com.ecommercebackoffice.customer.controller;

import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.customer.dto.*;
import com.ecommercebackoffice.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    //@ModelAttribute는 리퀘스트 파람을 여러개 묶어 쓸 수 있다.
    // page, size, sortBy, sortOrder 같은 query parameter를 요청 DTO로 바인딩한다.
    public ResponseEntity<PageResponse<CustomerGetResponse>> getCustomers(
            @ModelAttribute CustomerGetRequest request
    ) {
        Page<CustomerGetResponse> customersPage = customerService.getCustomersList(request);
        return ResponseEntity.ok(new PageResponse<>(customersPage));
    }

    // 고객 단건 조회
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerGetResponse> getOneCustomer(
            @PathVariable Long customerId
    ) {
        CustomerGetResponse result = customerService.getByCustomerId(customerId);

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
