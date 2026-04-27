package com.ecommercebackoffice.product.controller;

import com.ecommercebackoffice.product.dto.*;
import com.ecommercebackoffice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductCreateResponse> productCreate(
            @RequestBody ProductCreateRequest request
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetOneResponse> productGetOne(
            @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findOne(productId));
    }

    @GetMapping()
    public ResponseEntity<ProductGetAllResponse> productGetAll(
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponse> productInfoUpdate(
            @PathVariable Long productId,
            @RequestBody ProductInfoUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateInfo(productId, request));
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ProductUpdateResponse> productStockUpdate(
        @PathVariable Long productId,
        @RequestBody ProductStockUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateStock(productId, request));
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<ProductUpdateResponse> productStatusUpdate(
            @PathVariable Long productId,
            @RequestBody ProductStatusUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateStatus(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDeleteResponse> productDelete(
            @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.delete(productId));
    }
}
