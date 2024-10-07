package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.FailureType;
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
    public ResponseEntity<ProductDto> addProduct(@RequestBody CreateProductRequest createProductRequest) throws Exception {
        ProductDto newProduct = productService.addProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping("all-products")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> allProducts = productService.retrieveAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
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
}