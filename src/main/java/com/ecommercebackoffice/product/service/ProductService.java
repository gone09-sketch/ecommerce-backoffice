package com.ecommercebackoffice.product.service;

import com.ecommercebackoffice.exception.DuplicateProductException;
import com.ecommercebackoffice.exception.UnauthorizedException;
import com.ecommercebackoffice.product.dto.ProductCreateRequest;
import com.ecommercebackoffice.product.dto.ProductCreateResponse;
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
            throw new DuplicateProductException("이미 등록된 상품입니다.");
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
}
