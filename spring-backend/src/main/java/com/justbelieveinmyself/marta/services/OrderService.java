package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.OrderDto;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        Map<ProductDto, Integer> productAndQuantity = orderDto.getProductAndQuantity();
        Set<OrderProduct> orderProduct = new HashSet<>();
        for (ProductDto pd: productAndQuantity.keySet()) {
            Product product = productMapper.dtoToModel(pd);
            OrderProduct op = new OrderProduct();
            op.setProduct(product);
            op.setQuantity(productAndQuantity.get(pd));
            op.setOrder(order);
            orderProduct.add(op);
        }
        order.setOrderProduct(orderProduct);
        user.getOrders().add(order);
        orderRepository.save(order);
        userRepository.save(user);
        return ResponseEntity.ok(user.getOrders());
    }
}
