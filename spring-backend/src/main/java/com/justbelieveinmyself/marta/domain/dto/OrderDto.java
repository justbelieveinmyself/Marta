package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.OrderProduct;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Map<ProductDto, Integer> productAndQuantity;
    private ZonedDateTime orderedAt;
    private DeliveryStatus status;
    private Boolean isPaid;
    private SellerDto customer;
    public OrderDto of(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setStatus(order.getStatus());
        orderDto.setIsPaid(order.getIsPaid());
        Map<ProductDto, Integer> productAndQuantity = order.getOrderProduct().stream().collect(Collectors.toMap(orderItem -> ProductDto.of(orderItem.getProduct()), OrderProduct::getQuantity));
        orderDto.setProductAndQuantity(productAndQuantity);
        orderDto.setOrderedAt(order.getOrderedAt());
        orderDto.setCustomer(SellerDto.of(order.getCustomer()));
        return orderDto;
    }
}
