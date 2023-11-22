package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.OrderProduct;
import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDto {
    private Long id;
    private Map<Long, Integer> productIdAndQuantity;
    private ZonedDateTime orderedAt;
    private DeliveryStatus status;
    private Boolean isPaid;
    private SellerDto customer;
    public static OrderDto of(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setStatus(order.getStatus());
        orderDto.setIsPaid(order.getIsPaid());
        Map<Long, Integer> productAndQuantity = order.getOrderProduct().stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getId(), OrderProduct::getQuantity));
        orderDto.setProductIdAndQuantity(productAndQuantity);
        orderDto.setOrderedAt(order.getOrderedAt());
        orderDto.setCustomer(SellerDto.of(order.getCustomer()));
        return orderDto;
    }
}
