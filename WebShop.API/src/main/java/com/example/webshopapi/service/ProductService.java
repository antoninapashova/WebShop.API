package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;

import java.util.List;

public interface ProductService {
    ProductDto addProduct(CreateProductRequest createProductRequest) throws Exception;
    List<ProductDto> retrieveAllProducts();
    void initProducts();
    ExecutionResult deleteProduct(String productId);
}
