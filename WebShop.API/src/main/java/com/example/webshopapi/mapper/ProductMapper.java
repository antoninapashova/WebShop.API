package com.example.webshopapi.mapper;

import com.example.webshopapi.config.BaseMapper;
import com.example.webshopapi.dto.PromotionProductDto;
import com.example.webshopapi.projection.Product;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public abstract class ProductMapper {
   public abstract PromotionProductDto toDto(Product product);
}
