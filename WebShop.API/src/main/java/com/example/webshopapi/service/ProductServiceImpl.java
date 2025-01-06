package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.ImageDto;
import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.PromotionProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.dto.requestObjects.UpdateProductRequest;
import com.example.webshopapi.entity.CategoryEntity;
import com.example.webshopapi.entity.ImageEntity;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.mapper.ProductMapper;
import com.example.webshopapi.projection.Product;
import com.example.webshopapi.repository.CategoryRepository;
import com.example.webshopapi.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.webshopapi.service.OrderServiceImpl.CUSTOM_FORMATTER;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;
    ProductMapper mapper;

    @Override
    public ExecutionResult addProduct(CreateProductRequest createProductRequest) {
        boolean isExists = isProductExist(createProductRequest.name);

        if (isExists) {
            throw new EntityExistsException(String.format("Product with name %s already exists", createProductRequest.name));
        }

        ProductEntity product = modelMapper.map(createProductRequest, ProductEntity.class);

        CategoryEntity category = categoryRepository.findById(UUID.fromString(createProductRequest.getCategoryId()))
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
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
    public List<PromotionProductDto> retrieveAllProducts() {
        List<Product> result = productRepository.findAllProducts();
        return result.stream().map(this::asDto).toList();
    }

    @Override
    public ExecutionResult deleteProduct(String productId) {
        ProductEntity product = findProductById(UUID.fromString(productId));

        product.setDeleted(true);
        productRepository.save(product);

        return new ExecutionResult("Product removed successfully!");
    }

    @Override
    public ExecutionResult updateProduct(String productId, UpdateProductRequest product) {
        ProductEntity productEntity = findProductById(UUID.fromString(productId));

        CategoryEntity category = categoryRepository.findById(UUID.fromString(product.getCategoryId()))
                .orElseThrow(() -> new EntityNotFoundException("Category not found!"));

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
    public ProductDto getProductById(String productId) {
        ProductEntity product = findProductById(UUID.fromString(productId));
        return asDto(product);
    }

    @Override
    public List<ProductDto> fetchOnlyNonePromotionalProducts(){
        return productRepository.findAllNonPromotionalProducts()
                .stream()
                .map(this::asDto)
                .toList();
    }

    private ProductEntity findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private ProductDto asDto(ProductEntity productEntity) {
        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        productDto.setId(productEntity.getId().toString());
        productDto.setCategoryName(productEntity.getCategory().getName());
        List<ImageDto> imageDtos = productEntity.getImages().stream()
                .map(imageEntity -> modelMapper.map(imageEntity, ImageDto.class)).toList();
        productDto.setImages(imageDtos);

        return productDto;
    }

    private PromotionProductDto asDto(Product product) {
        PromotionProductDto productDto = mapper.toDto(product);

        String endDate = product.getEndDate() != null ? product.getEndDate().format(CUSTOM_FORMATTER) : "";
        productDto.setEndDate(endDate);

        return productDto;
    }

    private boolean isProductExist(String name) {
        return productRepository.existsByName(name);
    }
}