package com.ecommercebackoffice.customer.service;

import com.ecommercebackoffice.customer.dto.*;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 고객 리스트 조회
    @Transactional(readOnly = true)
    public Page<CustomerGetResponse> getCustomersList(CustomerGetListRequest request) {

        Pageable pageable = request.toPageable();//?page=1&size=10&sortBy=id&sortOrder=asc 이런 걸 JPA가 알아들을 수 있는 Pageable 객체로 바꿈

        String sortBy = request.getSortBy() == null || request.getSortBy().isBlank()
                ? "createdAt" : request.getSortBy();


        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortOrder())
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 검색어가 비어 있으면 null로 보내고, 값이 있으면 그 값을 그대로 보냄
        String keyword = null;
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            keyword = request.getKeyword();
        }

        // 문자열 "ACTIVE"를 enum CustomerStatus.ACTIVE로 바꿈
        CustomerStatus status = null;
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            status = CustomerStatus.valueOf(request.getStatus().toUpperCase());
        }

        return customerRepository.searchCustomers(keyword, status, pageable)
                .map(CustomerGetResponse::from);
    }

    // 고객 단 건 조회
    @Transactional(readOnly = true)
    public CustomerGetResponse getByCustomerId(Long customerId) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("존재하지 않는 고객입니다")
        );

        return CustomerGetResponse.from(foundCustomer);
    }

    // 고객 수정
    @Transactional
    public CustomerUpdateResponse update(Long customerId, CustomerUpdateRequest request) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("존재하지 않는 고객입니다")
        );

        foundCustomer.update(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber()
        );

        return CustomerUpdateResponse.from(foundCustomer);
    }

    // 고객 상태 수정
    @Transactional
    public CustomerStatusUpdateResponse statusUpdate(Long customerId, CustomerStatusUpdateRequest request) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("존재하지 않는 고객입니다")
        );

        foundCustomer.updateStatus(request.getStatus());

        return CustomerStatusUpdateResponse.from(foundCustomer);
    }

    // 고객 삭제
    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("존재하지 않는 고객입니다")
        );

        // BaseEntity의 @SoftDelete 설정에 따라 실제 DB에서는 del_yn 값이 변경된다.
        customerRepository.delete(foundCustomer);
    }


}
