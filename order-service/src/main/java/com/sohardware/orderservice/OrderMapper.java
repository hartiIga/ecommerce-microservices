package com.sohardware.orderservice;

import com.sohardware.orderservice.dto.OrderRequest;
import com.sohardware.orderservice.dto.OrderResponse;
import com.sohardware.orderservice.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // Permet de l'injecter avec @Autowired
public interface OrderMapper {
    OrderResponse toResponse(OrderEntity entity);
    OrderRequest toRequest(OrderEntity entity);
    OrderEntity toEntity(OrderRequest request);
    OrderEntity toEntity(OrderResponse response);

}
