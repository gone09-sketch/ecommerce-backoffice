package com.ecommercebackoffice.product.controller;

import com.ecommercebackoffice.common.CommonResponse;
import com.ecommercebackoffice.common.PageResponse;
import com.ecommercebackoffice.product.dto.*;
import com.ecommercebackoffice.product.service.ProductService;
import com.ecommercebackoffice.session.SessionAdmin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // 상품 등록 API
    @PostMapping
    public ResponseEntity<CommonResponse<ProductCreateResponse>> productCreate(
            @RequestBody @Valid ProductCreateRequest request,
            HttpServletRequest httpServletRequest
    ) {
        SessionAdmin sessionAdmin = (SessionAdmin) httpServletRequest.getSession()
                .getAttribute("loginAdmin");

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.created("상품 등록 성공", productService.create(sessionAdmin, request)));
    }

    // 상품 단 건 조회 API
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductGetOneResponse>> productGetOne(
            @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 상세 조회 성공", productService.findOne(productId)));
    }

    // 상품 전체 조회 API
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<ProductGetAllResponse>>> productGetAll(
            @ModelAttribute ProductGetAllRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 리스트 조회 성공", productService.findAll(request)));
    }

    // 상품 정보 수정 API
    @PatchMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductUpdateResponse>> productInfoUpdate(
            @PathVariable Long productId,
            @RequestBody ProductInfoUpdateRequest request   // 정보 수정 request의 경우, 이름, 카테고리, 가격 중 선택 수정이라서 따로 Validation 안 달았습니다.
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 정보 수정 성공", productService.updateInfo(productId, request)));
    }

    // 상품 재고 수정 API
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<CommonResponse<ProductUpdateResponse>> productStockUpdate(
            @PathVariable Long productId,
            @RequestBody @Valid ProductStockUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 재고 수정 성공", productService.updateStock(productId, request)));
    }

    // 상품 상태 수정 API
    @PatchMapping("/{productId}/status")
    public ResponseEntity<CommonResponse<ProductUpdateResponse>> productStatusUpdate(
            @PathVariable Long productId,
            @RequestBody @Valid ProductStatusUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 상태 수정 성공", productService.updateStatus(productId, request)));
    }

    // 상품 삭제 API
    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse<Void>> productDelete(
            @PathVariable Long productId
    ) {
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("고객 삭제 성공"));
    }
}