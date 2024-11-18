package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.dto.requestObjects.UpdateProductRequest;
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
    public ResponseEntity<?> addProduct(@ModelAttribute CreateProductRequest createProductRequest) throws Exception {
        ExecutionResult result = productService.addProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> allProducts = productService.retrieveAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getAllProductsByName(@PathVariable String name) {
        List<ProductDto> productsByName = productService.fetchAllByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(productsByName);
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        ExecutionResult result = productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(result.getMessage());
    }

    @PutMapping("/update-product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @ModelAttribute UpdateProductRequest product) {
        ExecutionResult result = productService.updateProduct(productId, product);

        if (result.getFailureType() == FailureType.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get-product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        ProductDto result = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}