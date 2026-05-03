package com.ecommercebackoffice.product.repository;

import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.enums.ProductStatus;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(@NotBlank String name);

    @Query("""
                SELECT p FROM Product p
                JOIN p.admin a
                WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
                AND (:category IS NULL OR p.category = :category)
                AND (:status IS NULL OR p.status = :status)
            """)

        // if) Param 값이 모두 null 이면 그냥 true이므로, 걍 전체 조회 기능이 된다
    Page<Product> searchProducts(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("status") ProductStatus status,
            Pageable pageable
    );

    // 주문 생성 시 재고 차감 동시성 제어를 위한 비관적 락 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByIdWithPessimisticLock(@Param("productId") Long productId);
}
