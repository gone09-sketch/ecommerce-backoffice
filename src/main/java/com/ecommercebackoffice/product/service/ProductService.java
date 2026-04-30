package com.ecommercebackoffice.product.service;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.common.dto.PageResponse;
import com.ecommercebackoffice.exception.AdminNotFoundException;
import com.ecommercebackoffice.exception.ProductDuplicateException;
import com.ecommercebackoffice.exception.ProductNotFoundException;
import com.ecommercebackoffice.product.dto.*;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 상품 등록
    @Transactional
    public ProductCreateResponse create(Long loginAdminId, ProductCreateRequest request) {

        Admin admin = adminRepository.findById(loginAdminId).orElseThrow(
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
        // JPA pageable로 변환(정렬순서, 정렬기준, 페이지 로직)
        Pageable pageable = request.toPageable();

        // 검색어가 공백인 경우 전체 조회를 위해 null 처리
        String keyword;
        if (StringUtils.hasText(request.getKeyword())) {
            // 값이 있으면 앞뒤 공백 제거해서 저장
            keyword = request.getKeyword().trim();
        } else {
            // null이거나 빈 문자열이면 null로 저장
            keyword = null;
        }

        // 카테고리가 공백인 경우 전체 조회를 위해 null 처리
        String category;
        if (StringUtils.hasText(request.getCategory())) {
            // 값이 있으면 앞뒤 공백 제거해서 저장
            category = request.getCategory().trim();
        } else {
            // null이거나 빈 문자열이면 null로 저장
            category = null;
        }

        // DB에서 Product 엔티티 목록 조회
        Page<Product> productPage = productRepository.searchProducts(
                keyword,
                category,
                request.getStatus(),
                pageable
        );

        // 최종 반환
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
