package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.dto.requestObjects.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    ExecutionResult addProduct(CreateProductRequest createProductRequest) throws Exception;
    List<ProductDto> retrieveAllProducts();
    ExecutionResult deleteProduct(String productId);
    ExecutionResult updateProduct(String productId, UpdateProductRequest productD);
    List<ProductDto> fetchAllByName(String name);
    ProductDto getProductById(String productId);
}
