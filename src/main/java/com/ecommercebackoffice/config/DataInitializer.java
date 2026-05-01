package com.ecommercebackoffice.config;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.enums.CustomerStatus;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.entity.OrderProduct;
import com.ecommercebackoffice.order.repository.OrderProductRepository;
import com.ecommercebackoffice.order.repository.OrderRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.enums.ProductStatus;
import com.ecommercebackoffice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String @NonNull ... args) {
        if (adminRepository.count() > 0
                || customerRepository.count() > 0
                || productRepository.count() > 0
                || orderRepository.count() > 0) {
            return;
        }

        initAdmins();
        initCustomers();
        initProducts();
        initOrders();
    }

    private void initAdmins() {
        AdminRole[] roles = {AdminRole.CS_ADMIN, AdminRole.OPERATION_ADMIN};

        IntStream.rangeClosed(1, 20).forEach(i -> {
            AdminRole role;
            String name;
            String email;

            if (i == 1) {
                role = AdminRole.SUPER_ADMIN;
                name = "슈퍼관리자";
                email = "super@test.com";
            } else {
                role = roles[(i - 2) % roles.length]; // 2가지 역할을 순환 배정
                name = "관리자" + i;
                email = "admin" + i + "@test.com";
            }

            Admin admin = new Admin(
                    name,
                    email,
                    passwordEncoder.encode("12345678"),
                    "010-1234-" + String.format("%04d", i),
                    role
            );

            // 다양한 테스트 환경을 위해 상태(승인/거절)를 섞어서 저장합니다.
            if (i == 1 || i % 2 == 0) {
                admin.approve(); // 1번과 짝수 번호는 승인 완료
            } else if (i % 5 == 0) {
                admin.reject("서류 미비 및 권한 불충분"); // 5의 배수는 승인 거부
            }
            // 그 외 홀수 번호는 대기(기본값) 상태 유지

            adminRepository.save(admin);
        });
    }

    private void initCustomers() {
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

    private void initProducts() {
        String[] categories = {"전자기기", "의류", "식품", "도서", "생활용품"};
        List<Admin> admins = adminRepository.findAll();

        IntStream.rangeClosed(1, 40).forEach(i -> {
            int stock = i % 5 == 0 ? 0 : i * 10;
            ProductStatus status = stock == 0 ? ProductStatus.SOLD_OUT : ProductStatus.ON_SALE;

            Product product = new Product(
                    "상품" + i,
                    categories[(i - 1) % categories.length],
                    1000L * i,
                    stock,
                    status,
                    admins.get((i - 1) % admins.size())
            );

            productRepository.save(product);
        });
    }

    private void initOrders() {
        List<Admin> admins = adminRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();

        IntStream.rangeClosed(1, 20).forEach(i -> {
            Customer customer = customers.get((i - 1) % customers.size());
            Product product = products.get((i - 1) % products.size());
            int quantity = i % 3 + 1;
            Admin orderAdmin = i % 2 == 0 ? admins.get((i - 1) % admins.size()) : null;

            Order order = new Order(
                    orderAdmin,
                    customer,
                    "수령인" + i,
                    "010-5000-" + String.format("%04d", i),
                    "서울시 테스트구 테스트로 " + i
            );

            product.descStock(quantity);

            Order savedOrder = orderRepository.save(order);
            OrderProduct orderProduct = new OrderProduct(
                    savedOrder,
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice()
            );

            orderProductRepository.save(orderProduct);
        });
    }
}