package com.ecommercebackoffice.customer.service;

import com.ecommercebackoffice.customer.dto.*;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 고객 리스트 조회
    @Transactional(readOnly = true)
    public Page<CustomerGetResponse> getCustomersList(CustomerGetListRequest request) {

        String sortBy = request.getSortBy() == null || request.getSortBy().isBlank()
                ? "createdAt" : request.getSortBy();

        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortOrder())
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(direction, sortBy)
        );

        return customerRepository.findAll(pageable)
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

        foundCustomer.update(request);

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

        customerRepository.delete(foundCustomer);
    }


}
