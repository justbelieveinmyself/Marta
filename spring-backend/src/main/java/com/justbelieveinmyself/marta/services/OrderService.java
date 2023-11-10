package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.OrderDto;
import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.OrderProduct;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.repositories.OrderRepository;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    public ResponseEntity<?> getListOrders(User user) {
        return ResponseEntity.ok(user.getOrders());
    }

    public ResponseEntity<?> createOrder(User user, OrderDto orderDto) {
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
        return ResponseEntity.ok(OrderDto.of(savedOrder));
    }
}
