package com.ecommercebackoffice.config;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.enums.AdminRole;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.customer.entity.Customer;
import com.ecommercebackoffice.customer.repository.CustomerRepository;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import com.ecommercebackoffice.order.entity.Order;
import com.ecommercebackoffice.order.entity.OrderProduct;
import com.ecommercebackoffice.order.repository.OrderRepository;
import com.ecommercebackoffice.order.repository.OrderProductRepository;
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
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    @Transactional
    public void run(String... args) {
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
        IntStream.rangeClosed(1, 3).forEach(i -> {
            Admin admin = new Admin(
                    "관리자" + i,
                    "admin" + i + "@test.com",
                    "password" + i,
                    "010-9999-" + String.format("%04d", i),
                    AdminRole.CS_ADMIN
            );
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
        Admin admin = adminRepository.findAll().get(0);
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Product product = new Product(
                    "상품" +i,
                    "전자",
                    10000L * i,
                    10 * i,
                    admin
            );
            productRepository.save(product);
        });
    }
    private void initOrders() {
        Admin admin = adminRepository.findAll().get(0);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            Customer customer = customerRepository.findById((long) i).orElseThrow(
                    () -> new IllegalStateException("고객 더미데이터가 없습니다.")
            );

            Product product = productRepository.findById((long) i).orElseThrow(
                    () -> new IllegalStateException("상품 더미데이터가 없습니다.")
            );

            int quantity = i % 3 + 1;
            Long orderPrice = product.getPrice();

            // 짝수 주문은 관리자가 등록, 홀수 주문은 고객이 직접 등록한 주문으로 처리
            Admin orderAdmin = (i % 2 == 0) ? admin : null;

            Order order = new Order(
                    orderAdmin,
                    customer,
                    quantity,
                    orderPrice,
                    "수령인" + i,
                    "010-5000-" + String.format("%04d", i),
                    "서울시 테스트구 테스트로 " + i
            );

            product.updateStock(quantity);

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
