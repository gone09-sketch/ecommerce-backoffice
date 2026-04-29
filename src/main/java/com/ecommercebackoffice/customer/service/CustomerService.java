package com.ecommercebackoffice.customer.service;

import com.ecommercebackoffice.customer.dto.*;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.exception.CustomerNotFoundException;
import com.ecommercebackoffice.exception.DuplicateEmailException;
import com.ecommercebackoffice.exception.InvalidInputException;
import com.ecommercebackoffice.order.enums.OrderStatus;
import com.ecommercebackoffice.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderProductRepository orderProductRepository;


    // 고객 리스트 조회
    @Transactional(readOnly = true)
    public Page<CustomerGetResponse> getCustomersList(CustomerGetRequest request) {

        // 클라이언트가 요청할 수 있는 정렬 필드만 허용하는 필드 만듬
        Set<String> allowedSortFields = Set.of("createdAt", "name", "email");

        // 정렬 기준이 없거나 허용되지 않은 값이면 기본값(createdAt)으로 정렬한다.
        String sortBy = request.getSortBy();
        if (sortBy == null || sortBy.isBlank() || !allowedSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        // sortOrder가 asc면 오름차순, 그 외에는 기본적으로 내림차순 처리한다.
        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortOrder())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // 클라이언트는 1페이지부터 요청하므로, JPA의 0-based 페이지 번호로 변환한다.
        Pageable pageable = PageRequest.of(
                Math.max(0, request.getPage() - 1),
                request.getSize(),
                Sort.by(direction, sortBy)
        );

        // 검색어가 비어 있으면 null로 보내고, 값이 있으면 그 값을 그대로 보냄 (전체 조회)
        String keyword = null;
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            keyword = request.getKeyword().trim();
        }

        // 문자열 "ACTIVE"를 enum CustomerStatus.ACTIVE로 바꿈
        CustomerStatus status = null;
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            try {
                status = CustomerStatus.valueOf(request.getStatus().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidInputException();
            }
        }

        Page<Customer> customersPage = customerRepository.searchCustomers(keyword, status, pageable);

        List<Long> customerIds = customersPage.getContent().stream()
                .map(Customer::getId)
                .toList();

        if (customerIds.isEmpty()) {
            return customersPage.map(customer -> CustomerGetResponse.from(customer, 0L, 0L));
        }

        Map<Long, CustomerOrderStats> statsMap = orderProductRepository.findOrderStatsByCustomerIds(customerIds, OrderStatus.CANCELED)

                .stream()
                .collect(Collectors.toMap(
                        CustomerOrderStats::getCustomerId,
                        Function.identity()
                ));

        return customersPage.map(customer -> {
            CustomerOrderStats stats = statsMap.get(customer.getId());

            long totalOrderCount = stats == null ? 0L : stats.getTotalOrderCount();
            long totalPurchaseAmount = stats == null ? 0L : stats.getTotalPurchaseAmount();

            return CustomerGetResponse.from(customer, totalOrderCount, totalPurchaseAmount);
        });

    }

    // 고객 단 건 조회
    @Transactional(readOnly = true)
    public CustomerGetResponse getByCustomerId(Long customerId) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException()
        );

        CustomerOrderStats stats = orderProductRepository.findOrderStatsByCustomerId(customerId, OrderStatus.CANCELED)
                .orElse(new CustomerOrderStats(customerId, 0L, 0L));

        return CustomerGetResponse.from(
                foundCustomer,
                stats.getTotalOrderCount(),
                stats.getTotalPurchaseAmount()
        );
    }

    // 고객 수정
    @Transactional
    public CustomerUpdateResponse update(Long customerId, CustomerUpdateRequest request) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException()
        );

        if (customerRepository.existsByEmailAndIdNot(
                request.getEmail(),
                customerId
        )) {
            throw new DuplicateEmailException();
        }

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
                () -> new CustomerNotFoundException()
        );

        foundCustomer.updateStatus(request.getStatus());

        return CustomerStatusUpdateResponse.from(foundCustomer);
    }

    // 고객 삭제
    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer foundCustomer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException()
        );

        // BaseEntity의 @SoftDelete 설정에 따라 실제 DB에서는 del_yn 값이 변경된다.
        customerRepository.delete(foundCustomer);
    }


}
