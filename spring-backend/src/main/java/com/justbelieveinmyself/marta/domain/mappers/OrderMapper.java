package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.domain.dto.OrderDto;
import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.OrderProduct;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "productIdAndQuantity", ignore = true)
    OrderDto modelToDto(Order order);
    @AfterMapping
    default void modelToDto(OrderDto target, Order order){
        Map<Long, Integer> productAndQuantity = order.getOrderProduct().stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getId(), OrderProduct::getQuantity));
        target.setProductIdAndQuantity(productAndQuantity);
    }
}
