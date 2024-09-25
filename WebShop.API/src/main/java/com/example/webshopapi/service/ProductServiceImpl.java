package com.example.webshopapi.service;

import com.example.webshopapi.dto.ProductDto;
import com.example.webshopapi.dto.requestObjects.CreateProductRequest;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto addProduct(CreateProductRequest createProductRequest) throws Exception {
        boolean isExists = isProductExist(createProductRequest.name);

        if (isExists) {
            throw new Exception(String.format("Product with name %s already exists", createProductRequest.name));
        }

        ProductEntity product = modelMapper.map(createProductRequest, ProductEntity.class);
        productRepository.save(product);

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> retrieveAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::asDto).toList();
    }

    @Override
    public void initProducts() {
        if(productRepository.count() >  0) {
            return;
        }

        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("product1");
        productEntity.setQuantity(10);
        productEntity.setPrice(10.5);
        productEntity.setItems(new ArrayList<>());
        productRepository.save(productEntity);
    }

    private ProductDto asDto(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDto.class);
    }

    private boolean isProductExist(String name) {
        return productRepository.existsByName(name);
    }
}