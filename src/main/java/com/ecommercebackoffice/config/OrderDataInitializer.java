package com.ecommercebackoffice.config;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class OrderDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (adminRepository.count() > 0 || customerRepository.count() > 0 || productRepository.count() > 0) {
            return;
        }

        initAdmins();
        initCustomers();
        initProducts();
    }

    private void initAdmins() {
        IntStream.rangeClosed(1, 3).forEach(i -> {
            Admin admin = new Admin();
            adminRepository.save(admin);
        });
    }

    private void initCustomers() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Customer customer = new Customer(
                    "고객" + i,
                    "customer" + i + "@test.com",
                    "010-1234-" + String.format("%04d", i)
            );
            customerRepository.save(customer);
        });
    }

    private void initProducts() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Product product = new Product(
                    "상품" +i,
                    "전자",
                    10000L * i,
                    10 * i
            );
            productRepository.save(product);
        });
    }
}
