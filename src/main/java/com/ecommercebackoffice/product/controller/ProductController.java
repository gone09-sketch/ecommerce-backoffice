package com.ecommercebackoffice.product.controller;

import com.ecommercebackoffice.product.dto.ProductCreateRequest;
import com.ecommercebackoffice.product.dto.ProductCreateResponse;
import com.ecommercebackoffice.product.dto.ProductGetOneResponse;
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

}
