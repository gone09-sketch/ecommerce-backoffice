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
    public Page<CustomerGetResponse> getCustomersList(CustomerGetRequest customerPageRequest) {

        // 클라이언트가 요청할 수 있는 정렬 필드만 허용하는 필드 만듬
        Set<String> allowedSortFields = Set.of("createdAt", "name", "email");

        // 정렬 기준이 없거나 허용되지 않은 값이면 기본값(createdAt)으로 정렬한다.
        String sortBy = customerPageRequest.getSortBy();
        if (sortBy == null || sortBy.isBlank() || !allowedSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        // 2. pageable 생성 (공통화)
        Pageable pageable = customerPageRequest.toPageable(sortBy);

        // 검색어가 비어 있으면 null로 보내고, 값이 있으면 그 값을 그대로 보냄 (전체 조회)
        String keyword = null;
        if (customerPageRequest.getKeyword() != null && !customerPageRequest.getKeyword().isBlank()) {
            keyword = customerPageRequest.getKeyword().trim();
        }

        // 상태처리: 문자열 "ACTIVE"를 enum CustomerStatus.ACTIVE로 바꿈
        CustomerStatus status = null;
        if (customerPageRequest.getStatus() != null && !customerPageRequest.getStatus().isBlank()) {
            try {
                status = CustomerStatus.valueOf(customerPageRequest.getStatus().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidInputException();
            }
        }
        // 고객 조회
        Page<Customer> customersPage = customerRepository.searchCustomers(keyword, status, pageable);

        // 페이지 범위 검증
        customerPageRequest.validatePageRange(customersPage.getTotalPages());

        // 고객 ID 추출
        List<Long> customerIds = customersPage.getContent().stream()
                .map(Customer::getId)
                .toList();

        // 주문 통계 없을 경우
        if (customerIds.isEmpty()) {
            return customersPage.map(customer -> CustomerGetResponse.from(customer, 0L, 0L));
        }

        // 주문 통계 조회
        Map<Long, CustomerOrderStatus> statsMap = orderProductRepository.findOrderStatsByCustomerIds(customerIds, OrderStatus.CANCELED)

                .stream()
                .collect(Collectors.toMap(
                        CustomerOrderStatus::getCustomerId,
                        Function.identity()
                ));

        // 응답 매핑
        return customersPage.map(customer -> {
            CustomerOrderStatus stats = statsMap.get(customer.getId());

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

        CustomerOrderStatus stats = orderProductRepository.findOrderStatsByCustomerId(customerId, OrderStatus.CANCELED)
                .orElse(new CustomerOrderStatus(customerId, 0L, 0L));

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

        if (!foundCustomer.getEmail().equals(request.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {
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
