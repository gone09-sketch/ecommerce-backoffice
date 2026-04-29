package com.ecommercebackoffice.product.service;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.exception.AdminNotFoundException;
import com.ecommercebackoffice.exception.ProductDuplicateException;
import com.ecommercebackoffice.exception.ProductNotFoundException;
import com.ecommercebackoffice.product.dto.*;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import com.ecommercebackoffice.auth.session.SessionAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 상품 등록
    @Transactional
    public ProductCreateResponse create(SessionAdmin sessionAdmin, ProductCreateRequest request) {

        Admin admin = adminRepository.findById(sessionAdmin.getId()).orElseThrow(
                () -> new AdminNotFoundException()
        );

        if (productRepository.existsByName(request.getName())) {
            throw new ProductDuplicateException("이미 등록된 상품입니다.");
        }

        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock(),
                request.getStatus(),
                admin
        );

        Product savedProduct = productRepository.save(product);

        return ProductCreateResponse.from(savedProduct);
    }

    // 상품 단 건 조회
    @Transactional(readOnly = true)
    public ProductGetOneResponse findOne(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException()
        );

        return ProductGetOneResponse.from(product);
    }

    // 상품 전체 조회
    @Transactional(readOnly = true)
    public PageResponse<ProductGetAllResponse> findAll(ProductGetAllRequest request) {

        // 클라이언트가 요청할 수 있는 정렬 필드만 허용하는 필드 만듬
        Set<String> allowedSortFields = Set.of("price", "stock", "createdAt");

        // 정렬 기준이 없거나 허용되지 않은 값이면 기본값(createdAt)으로 정렬한다.
        String sortBy = request.getSortBy();
        if (sortBy == null || sortBy.isBlank() || !allowedSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        // sortOrder가 asc면 오름차순, 그 외에는 기본적으로 내림차순 처리한다.
        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortOrder())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // 클라이언트는 1페이지부터 요청하므로, JPA의 0-based 페이지 번호로 변환한다.
        Pageable pageable = PageRequest.of(
                Math.max(0, request.getPage() - 1),
                request.getSize(),
                Sort.by(direction, sortBy)
        );

        Page<Product> productPage = productRepository.searchProducts(
                request.getKeyword(),
                request.getCategory(),
                request.getStatus(),
                pageable
        );

        return new PageResponse<>(productPage.map(ProductGetAllResponse::from));
    }

    // 상품 정보 수정
    @Transactional
    public ProductUpdateResponse updateInfo(Long productId, ProductInfoUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException()
        );

        product.updateInfo(request.getName(), request.getCategory(), request.getPrice());

        return ProductUpdateResponse.from(product);
    }

    // 상품 재고 수정
    @Transactional
    public ProductUpdateResponse updateStock(Long productId, ProductStockUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException()
        );

        product.updateStock(request.getStock());

        return ProductUpdateResponse.from(product);
    }

    // 상품 상태 수정
    @Transactional
    public ProductUpdateResponse updateStatus(Long productId, ProductStatusUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException()
        );

        product.updateStatus(request.getStatus());

        return ProductUpdateResponse.from(product);
    }

    // 상품 삭제
    @Transactional
    public void delete(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException()
        );

        productRepository.delete(product);
    }
}
