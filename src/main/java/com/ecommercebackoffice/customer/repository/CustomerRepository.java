package com.ecommercebackoffice.customer.repository;

import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("""
        select c
        from Customer c
        where (:keyword is null or c.name Like %:keyword% or c.email Like %:keyword%)
          and (:status is null or c.status = :status)
    """)
    Page<Customer> searchCustomers(
            @Param("keyword") String keyword,
            @Param("status") CustomerStatus status,
            Pageable pageable
    );
}
