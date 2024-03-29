package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.UserRightsValidator;
import com.justbelieveinmyself.marta.domain.dto.OrderDto;
import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.OrderProduct;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import com.justbelieveinmyself.marta.domain.mappers.OrderMapper;
import com.justbelieveinmyself.marta.repositories.OrderRepository;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserRightsValidator userRightsValidator;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository, OrderMapper orderMapper, UserRightsValidator userRightsValidator) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.userRightsValidator = userRightsValidator;
    }

    public ResponseEntity<List<OrderDto>> getListOrders(User user) {
        List<OrderDto> list = user.getOrders().stream().map(orderMapper::modelToDto).toList();
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<OrderDto> createOrder(User user, OrderDto orderDto) {
        Order order = new Order();
        order.setCustomer(user);
        order.setStatus(DeliveryStatus.AWAITING_CONFIRMATION);
        order.setIsPaid(orderDto.getIsPaid());
        Set<OrderProduct> orderProductSet = new HashSet<>();
        Map<Long, Integer> productAndQuantity = orderDto.getProductIdAndQuantity();
        for (Long productId: productAndQuantity.keySet()) {
            Product product = productRepository.findById(productId).get();
            OrderProduct op = new OrderProduct();
            op.setProduct(product);
            op.setQuantity(productAndQuantity.get(productId));
            op.setOrder(order);
            orderProductSet.add(op);
        }
        order.setOrderProduct(orderProductSet);

        Order savedOrder = orderRepository.save(order);
        user.getOrders().add(savedOrder);
        userRepository.save(user);
        return ResponseEntity.ok(orderMapper.modelToDto(savedOrder));
    }

    public ResponseEntity<OrderDto> changeOrderStatus(Order order, DeliveryStatus status, User authedUser) {
        userRightsValidator.validateRights(authedUser, order.getCustomer());
        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok(orderMapper.modelToDto(order));
    }

}
