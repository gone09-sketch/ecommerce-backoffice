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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public ProductCreateResponse create(HttpServletRequest httpServletRequest, ProductCreateRequest request) {

        SessionAdminDto sessionAdminDto = (SessionAdminDto) httpServletRequest.getSession()
                .getAttribute("loginAdmin");

        Admin admin = adminRepository.findById(sessionAdminDto.getId()).orElseThrow(
                () -> new AdminNotFoundException("해당 관리자를 찾을 수 없습니다.")
        );

        if(productRepository.existsByName(request.getName())) {
            throw new ProductDuplicateException("이미 등록된 상품입니다.");
        }

        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock(),
                admin
        );

        Product savedProduct = productRepository.save(product);

        return new ProductCreateResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getStatus().getStatus(),
                savedProduct.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public ProductGetOneResponse findOne(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        return new ProductGetOneResponse(
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().getStatus(),
                product.getCreatedAt(),
                product.getAdmin().getName(),
                product.getAdmin().getEmail()
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductGetAllResponse> findAll(ProductGetAllRequest request) {

        Pageable pageable = request.toPageable();

        Page<Product> productPage = productRepository.searchProducts(
                request.getKeyword(),
                request.getCategory(),
                request.getStatus(),
                pageable
        );

        List<ProductGetAllResponse> dtoDatas =  productPage.stream()
                .map(product -> new ProductGetAllResponse(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus().getStatus(),
                        product.getCreatedAt(),
                        product.getAdmin().getName()
                )).toList();

        // 👉 PageResponse로 바로 감싸기
        Page<ProductGetAllResponse> mappedPage =
                new org.springframework.data.domain.PageImpl<>(
                        dtoDatas,
                        pageable,
                        productPage.getTotalElements()
                );

        return new PageResponse<>(mappedPage);
    }

    @Transactional
    public ProductUpdateResponse updateInfo(Long productId, ProductInfoUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateInfo(request.getName(), request.getCategory(), request.getPrice());

        return new ProductUpdateResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().getStatus(),
                product.getUpdatedAt()
        );
    }

    @Transactional
    public ProductUpdateResponse updateStock(Long productId, ProductStockUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateStock(request.getStock());

        return new ProductUpdateResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().getStatus(),
                product.getUpdatedAt()
        );
    }

    @Transactional
    public ProductUpdateResponse updateStatus(Long productId, ProductStatusUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateStatus(request.getStatus());

        return new ProductUpdateResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().getStatus(),
                product.getUpdatedAt()
        );
    }

    @Transactional
    public void delete(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        productRepository.delete(product);
    }
}
