package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @PostMapping("add-product")
    public ResponseEntity<?> addProduct(@RequestBody CreateProductRequest createProductRequest) throws Exception {
        TypedResult<ProductDto> result = productService.addProduct(createProductRequest);

        if (result.getFailureType() == FailureType.UNKNOWN) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
    }

    @GetMapping("all-products")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> allProducts = productService.retrieveAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    @GetMapping("search/{name}")
    public ResponseEntity<?> getAllProductsByName(@PathVariable String name) {
        List<ProductDto> productsByName = productService.fetchAllByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(productsByName);
    }

    @DeleteMapping("delete-product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        if (productId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var result = productService.deleteProduct(productId);

        if (result.getFailureType() == FailureType.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result.getMessage());
    }

    @PutMapping("update-product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto) {
        if (productId == null || productDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var result = productService.updateProduct(productId, productDto);

        if (result.getFailureType() == FailureType.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result.getData());
    }
}