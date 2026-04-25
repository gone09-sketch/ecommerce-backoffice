package com.ecommercebackoffice.product.service;

import com.ecommercebackoffice.exception.ProductDuplicateException;
import com.ecommercebackoffice.exception.ProductNotFoundException;
import com.ecommercebackoffice.exception.UnauthorizedException;
import com.ecommercebackoffice.product.dto.*;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import com.ecommercebackoffice.session.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductCreateResponse create(SessionUser sessionUser, ProductCreateRequest request) {
        if(sessionUser == null) {
            throw new UnauthorizedException("로그인이 필요한 기능입니다.");
        }

        if(productRepository.existsByName(request.getName())) {
            throw new ProductDuplicateException("이미 등록된 상품입니다.");
        }

        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock()
        );

        Product savedProduct = productRepository.save(product);

        return new ProductCreateResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getStatus(),
                savedProduct.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public ProductGetOneResponse findOne(SessionUser sessionUser, Long productId) {
        if (sessionUser == null) {
            throw new UnauthorizedException("로그인이 필요한 기능입니다.");
        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        return new ProductGetOneResponse(
                200,
                "상품 상세 조회 성공",
                new ProductGetOneResult(
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus(),
                        product.getCreatedAt(),
                        product.getAdmin().getName(),
                        product.getAdmin().getEmail()
                )
        );
    }

    @Transactional
    public ProductUpdateResponse updateInfo(SessionUser sessionUser, Long productId, ProductInfoUpdateRequest request) {
        if(sessionUser == null) {
            throw new UnauthorizedException("로그인이 필요한 기능입니다.");
        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateInfo(request.getName(), request.getCategory(), request.getPrice());

        return new ProductUpdateResponse(
                200,
                "상품 정보 수정 성공",
                new ProductUpdateResult(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUpdatedAt()
                )
        );
    }

    @Transactional
    public ProductUpdateResponse updateStock(SessionUser sessionUser, Long productId, ProductStockUpdateRequest request) {
        if(sessionUser == null) {
            throw new UnauthorizedException("로그인이 필요한 기능입니다.");
        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateStock(request.getStock());

        return new ProductUpdateResponse(
                200,
                "상품 재고 변경 성공",
                new ProductUpdateResult(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUpdatedAt()
                )
        );
    }
}
