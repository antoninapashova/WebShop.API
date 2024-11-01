package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.ImageDto;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.dto.requestObjects.UpdateProductRequest;
import com.example.webshopapi.entity.CategoryEntity;
import com.example.webshopapi.entity.ImageEntity;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.repository.CategoryRepository;
import com.example.webshopapi.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
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
    public ExecutionResult addProduct(CreateProductRequest createProductRequest) throws Exception {
        boolean isExists = isProductExist(createProductRequest.name);

        if (isExists) {
            return new ExecutionResult(FailureType.UNKNOWN, String.format("Product with name %s already exists", createProductRequest.name));
        }

        ProductEntity product = modelMapper.map(createProductRequest, ProductEntity.class);

        CategoryEntity category = categoryRepository.findById(UUID.fromString(createProductRequest.getCategoryId())).orElse(null);
        product.setCategory(category);

        List<ImageEntity> images = Arrays.stream(createProductRequest.getImages()).map(img -> {
            try {
                return new ImageEntity(img.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        product.setImages(images);
        productRepository.save(product);

        return new ExecutionResult("Product added successfully!");
    }

    @Override
    public List<ProductDto> retrieveAllProducts() {
        return productRepository.findAll()
                .stream()
                .filter(product -> !product.isDeleted())
                .map(this::asDto).toList();
    }

    @Override
    public ExecutionResult deleteProduct(String productId) {
        ProductEntity productEntity = productRepository.findById(UUID.fromString(productId)).orElse(null);

        if (productEntity == null) {
            return new ExecutionResult(FailureType.NOT_FOUND, "Product not found!");
        }

        productEntity.setDeleted(true);
        productRepository.save(productEntity);

        return new ExecutionResult("Product removed successfully!");
    }

    @Override
    public ExecutionResult updateProduct(String productId, UpdateProductRequest product) {
        ProductEntity productEntity = productRepository.findById(UUID.fromString(productId)).orElse(null);

        if (productEntity == null) {
            return new ExecutionResult(FailureType.NOT_FOUND, "Product not found!");
        }

        CategoryEntity category = categoryRepository.findById(UUID.fromString(product.getCategoryId())).orElse(null);
        productEntity.setCategory(category);
        productEntity.setName(product.getName());
        productEntity.setQuantity(product.getQuantity());
        productEntity.setPrice(product.getPrice());
        productEntity.getImages().clear();

        if (product.getImages() != null) {
            List<ImageEntity> images = Arrays.stream(product.getImages()).map(img -> {
                try {
                    return new ImageEntity(img.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toList();

            productEntity.getImages().addAll(images);
        }

        productRepository.save(productEntity);

        return new ExecutionResult("Product updated successfully!");
    }

    @Override
    public List<ProductDto> fetchAllByName(String name) {
        return productRepository.findAllByName(name).stream().map(this::asDto).toList();
    }

    @Override
    public TypedResult<ProductDto> getProductById(String productId) {
        ProductEntity product = productRepository.findProductEntityById(UUID.fromString(productId));

        if (product == null) {
            return new TypedResult<>(FailureType.NOT_FOUND, "Product not found!");
        }

        return new TypedResult<>(asDto(product));
    }

    private ProductDto asDto(ProductEntity productEntity) {
        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        productDto.setId(productEntity.getId().toString());
        productDto.setCategoryName(productEntity.getCategory().getName());
        List<ImageDto> imageDtos = productEntity.getImages().stream().map(imageEntity -> modelMapper.map(imageEntity, ImageDto.class)).toList();
        productDto.setImages(imageDtos);

        return productDto;
    }

    private boolean isProductExist(String name) {
        return productRepository.existsByName(name);
    }
}