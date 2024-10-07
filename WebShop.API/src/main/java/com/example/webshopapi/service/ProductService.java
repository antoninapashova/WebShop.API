package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductDto addProduct(CreateProductRequest createProductRequest) throws Exception;
    List<ProductDto> retrieveAllProducts();
    void initProducts();
    ExecutionResult deleteProduct(String productId);
    TypedResult<ProductDto> updateProduct(String productId, ProductDto productDto);
}
