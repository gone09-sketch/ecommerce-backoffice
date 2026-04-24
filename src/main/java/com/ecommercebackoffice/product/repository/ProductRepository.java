package com.ecommercebackoffice.product.repository;

import com.ecommercebackoffice.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(@NotBlank String name);
}
