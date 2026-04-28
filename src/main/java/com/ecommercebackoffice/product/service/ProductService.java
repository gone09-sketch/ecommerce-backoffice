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
import com.ecommercebackoffice.session.SessionAdminDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 상품 등록
    @Transactional
    public ProductCreateResponse create(HttpServletRequest httpServletRequest, ProductCreateRequest request) {

        SessionAdminDto sessionAdminDto = (SessionAdminDto) httpServletRequest.getSession()
                .getAttribute("loginAdmin");

        Admin admin = adminRepository.findById(sessionAdminDto.getId()).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다.")
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
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        return ProductGetOneResponse.from(product);
    }

    // 상품 전체 조회
    @Transactional(readOnly = true)
    public PageResponse<ProductGetAllResponse> findAll(ProductGetAllRequest request) {

        Pageable pageable = request.toPageable();

        Page<Product> productPage = productRepository.searchProducts(
                request.getKeyword(),
                request.getCategory(),
                request.getStatus(),
                pageable
        );

        List<ProductGetAllResponse> dtoDatas = productPage.stream()
                .map(ProductGetAllResponse::from)
                .toList();

        // 👉 PageResponse로 바로 감싸기
        Page<ProductGetAllResponse> mappedPage =
                new PageImpl<>(
                        dtoDatas,
                        pageable,
                        productPage.getTotalElements()
                );

        return new PageResponse<>(mappedPage);
    }

    // 상품 정보 수정
    @Transactional
    public ProductUpdateResponse updateInfo(Long productId, ProductInfoUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateInfo(request.getName(), request.getCategory(), request.getPrice());

        return ProductUpdateResponse.from(product);
    }

    // 상품 재고 수정
    @Transactional
    public ProductUpdateResponse updateStock(Long productId, ProductStockUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateStock(request.getStock());

        return ProductUpdateResponse.from(product);
    }

    // 상품 상태 수정
    @Transactional
    public ProductUpdateResponse updateStatus(Long productId, ProductStatusUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateStatus(request.getStatus());

        return ProductUpdateResponse.from(product);
    }

    // 상품 삭제
    @Transactional
    public void delete(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        productRepository.delete(product);
    }
}
