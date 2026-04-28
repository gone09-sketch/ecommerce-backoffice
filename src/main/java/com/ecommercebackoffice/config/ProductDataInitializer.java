package com.ecommercebackoffice.config;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class ProductDataInitializer implements CommandLineRunner {

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
//        initCustomers();
        initProducts();
    }

    private void initAdmins() {
        IntStream.rangeClosed(1, 3).forEach(i -> {

            Admin admin = new Admin(
                    "관리자" + i,
                    "admin" + i + "@test.com",
                    "password" + i,
                    "010-0000-" + String.format("%04d", i),
                    AdminRole.CS_ADMIN
//                    i == 1 ? AdminRole.SUPER_ADMIN : AdminRole.ADMIN
            );

            // 상태 분기 (실제 테스트용)
            if (i == 1) {
                admin.approve(); // 활성 관리자
            } else if (i == 2) {
                admin.approve(); // 활성 관리자
            } else {
                admin.reject("서류 미비"); // 거절된 관리자
            }

            adminRepository.save(admin);
        });
    }

//    private void initCustomers() {
//        IntStream.rangeClosed(1, 10).forEach(i -> {
//            Customer customer = new Customer(
//                    "고객" + i,
//                    "customer" + i + "@test.com",
//                    "010-1234-" + String.format("%04d", i)
//            );
//            customerRepository.save(customer);
//        });
//    }

    private void initProducts() {
        String[] categories = {"전자기기", "의류", "식품", "도서", "생활용품"};

        List<Admin> admins = adminRepository.findAll();

        IntStream.rangeClosed(1, 20).forEach(i -> {

            Admin admin = admins.get(i % admins.size());

            Product product = new Product(
                    "상품" + i,
                    categories[i % categories.length],
                    1000L * i,
                    i % 5 == 0 ? 0 : (i * 10), // 일부는 품절
                    admin
            );

            productRepository.save(product);
        });
    }
}