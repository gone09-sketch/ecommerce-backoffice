package com.ecommercebackoffice.product.service;

import com.ecommercebackoffice.admin.entity.Admin;
import com.ecommercebackoffice.admin.repository.AdminRepository;
import com.ecommercebackoffice.exception.AdminNotFoundException;
import com.ecommercebackoffice.exception.ProductDuplicateException;
import com.ecommercebackoffice.exception.ProductNotFoundException;
import com.ecommercebackoffice.product.dto.*;
import com.ecommercebackoffice.product.entity.Product;
import com.ecommercebackoffice.product.repository.ProductRepository;
import com.ecommercebackoffice.session.SessionAdminDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
                .getAttribute("LOGIN_USER");

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
                200,
                "상품 상세 조회 성공",
                new ProductGetOneResult(
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus().getStatus(),
                        product.getCreatedAt(),
                        product.getAdmin().getName(),
                        product.getAdmin().getEmail()
                )
        );
    }

    @Transactional(readOnly = true)
    public ProductGetAllResponse findAll() {

        List<Product> products= productRepository.findAll();

        List<ProductGetAllResult> dtoDatas =  products.stream()
                .map(product -> new ProductGetAllResult(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus().getStatus(),
                        product.getCreatedAt(),
                        product.getAdmin().getName()
                )).toList();

        return new ProductGetAllResponse(
                200,
                "상품 리스트 조회 성공",
                dtoDatas
        );
    }

    @Transactional
    public ProductUpdateResponse updateInfo(Long productId, ProductInfoUpdateRequest request) {

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
                        product.getStatus().getStatus(),
                        product.getUpdatedAt()
                )
        );
    }

    @Transactional
    public ProductUpdateResponse updateStock(Long productId, ProductStockUpdateRequest request) {

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
                        product.getStatus().getStatus(),
                        product.getUpdatedAt()
                )
        );
    }

    @Transactional
    public ProductUpdateResponse updateStatus(Long productId, ProductStatusUpdateRequest request) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        product.updateStatus(request.getStatus());

        return new ProductUpdateResponse(
                200,
                "상품 상태 변경 성공",
                new ProductUpdateResult(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getStock(),
                        product.getStatus().getStatus(),
                        product.getUpdatedAt()
                )
        );
    }

    @Transactional
    public ProductDeleteResponse delete(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("해당하는 상품이 존재하지 않습니다.")
        );

        productRepository.delete(product);

        return new ProductDeleteResponse(
                204,
                "상품 삭제 성공"
        );
    }
}
