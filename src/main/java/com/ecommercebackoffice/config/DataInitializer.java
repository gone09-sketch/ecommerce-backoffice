package com.ecommercebackoffice.config;

import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;
@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            return;
        }

        IntStream.rangeClosed(1, 20).forEach(i -> {
            Customer customer = new Customer(
                    "고객" + i,
                    "customer" + i + "@test.com",
                    "010-1234-" + String.format("%04d", i)
            );

            if (i % 3 == 0) {
                customer.updateStatus(CustomerStatus.INACTIVE);
            }

            customerRepository.save(customer);
        });
    }
}
