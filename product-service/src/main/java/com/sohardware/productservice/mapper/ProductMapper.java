package com.sohardware.productservice.mapper;

import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // Permet de l'injecter avec @Autowired
public interface ProductMapper {
    ProductResponse toResponse(ProductEntity entity);
    ProductEntity toEntity(ProductResponse request);
}
