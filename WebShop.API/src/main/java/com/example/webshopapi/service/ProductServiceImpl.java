package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.entity.CategoryEntity;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.repository.CategoryRepository;
import com.example.webshopapi.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto addProduct(CreateProductRequest createProductRequest) throws Exception {
        boolean isExists = isProductExist(createProductRequest.name);

        if (isExists) {
            throw new Exception(String.format("Product with name %s already exists", createProductRequest.name));
        }

        CategoryEntity category = categoryRepository.findById(UUID.fromString(createProductRequest.getCategoryId())).orElse(null);

        ProductEntity product = modelMapper.map(createProductRequest, ProductEntity.class);
        product.setCategory(category);
        productRepository.save(product);

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> retrieveAllProducts() {
        return productRepository.findAll()
                .stream()
                .filter(product -> !product.isDeleted())
                .map(this::asDto).toList();
    }

    @Override
    public void initProducts() {
        if(productRepository.count() >  0) {
            return;
        }
        CategoryEntity category = categoryRepository.findByName("Sport accessories");
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("product1");
        productEntity.setQuantity(10);
        productEntity.setPrice(10.5);
        productEntity.setItems(new ArrayList<>());
        productEntity.setCategory(category);
        productRepository.save(productEntity);
    }

    @Override
    public ExecutionResult deleteProduct(String productId) {
        ProductEntity productEntity = productRepository.findById(UUID.fromString(productId)).orElse(null);

        if(productEntity == null) {
             return new ExecutionResult(FailureType.NOT_FOUND, "Product not found!");
        }

        productEntity.setDeleted(true);
        productRepository.save(productEntity);

        return new ExecutionResult("Product removed successfully");
    }

    @Override
    public TypedResult<ProductDto> updateProduct(String productId, ProductDto productDto) {
        ProductEntity productEntity = productRepository.findById(UUID.fromString(productId)).orElse(null);

        if(productEntity == null) {
            return new TypedResult<>(FailureType.NOT_FOUND, "Product not found!");
        }

        productEntity.setName(productDto.getName());
        productEntity.setQuantity(productDto.getQuantity());
        productEntity.setPrice(productDto.getPrice());

        productRepository.save(productEntity);

        return new TypedResult<>(asDto(productEntity));
    }

    private ProductDto asDto(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDto.class);
    }

    private boolean isProductExist(String name) {
        return productRepository.existsByName(name);
    }
}