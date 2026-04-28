package com.ecommercebackoffice.product.controller;

import com.ecommercebackoffice.product.dto.*;
import com.ecommercebackoffice.product.service.ProductService;
import com.ecommercebackoffice.session.SessionUser;
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
            @SessionAttribute(name = "loginAdmin", required = false) SessionUser sessionUser,
            @RequestBody ProductCreateRequest request
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(sessionUser, request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetOneResponse> productGetOne(
            @SessionAttribute(name = "loginAdmin", required = false) SessionUser sessionUser,
            @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findOne(sessionUser, productId));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponse> productInfoUpdate(
            @SessionAttribute(name = "loginAdmin", required = false) SessionUser sessionUser,
            @PathVariable Long productId,
            @RequestBody ProductInfoUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateInfo(sessionUser, productId, request));
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ProductUpdateResponse> productStockUpdate(
        @SessionAttribute(name = "loginAdmin", required = false) SessionUser sessionUser,
        @PathVariable Long productId,
        @RequestBody ProductStockUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateStock(sessionUser, productId, request));
    }

}
