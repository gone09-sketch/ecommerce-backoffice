package com.ecommercebackoffice.customer.service;

import com.ecommercebackoffice.customer.dto.*;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;


    // 고객 리스트 조회
    @Transactional(readOnly = true)
    public CustomerGetListResponse getCustomersList() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerGetListResponse.CustomerGetDto> customerGetDtoList = customers.stream()
                .map(CustomerGetListResponse.CustomerGetDto::from)
                .collect(Collectors.toList());
        return new CustomerGetListResponse(customerGetDtoList);
    }

    //고객 단 건 조회
    @Transactional(readOnly = true)
    public CustomerGetListResponse.CustomerGetDto getByCustomerId(Long customerId) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("존재하지 않는 고객입니다")
        );
        return CustomerGetListResponse.CustomerGetDto.from(foundCustomer);
    }


    // 고객 수정
    @Transactional
    public CustomerUpdateResponse update(Long customerId, CustomerUpdateRequest request) {

        Customer foundcustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("존재하지 않는 고객입니다")
        );
        foundcustomer.update(request);
        return CustomerUpdateResponse.from(foundcustomer);
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


    //고객 상태 수정



}
