package com.ecommercebackoffice.customer.repository;

import com.ecommercebackoffice.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {



}
