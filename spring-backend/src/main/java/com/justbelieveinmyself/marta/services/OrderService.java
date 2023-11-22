package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.OrderDto;
import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.OrderProduct;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;
import com.justbelieveinmyself.marta.repositories.OrderRepository;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public ResponseEntity<?> getListOrders(User user) {
        return ResponseEntity.ok(user.getOrders().stream().map(OrderDto::of).toList());
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

    public ResponseEntity<?> changeOrderStatus(Order order, String status, User authedUser) {
        validateRights(authedUser, order.getCustomer());
        order.setStatus(DeliveryStatus.valueOf(status));
        orderRepository.save(order);
        return ResponseEntity.ok(OrderDto.of(order));
    }

    private void validateRights(User userFromAuthToken, User userToEdit) { //TODO: refactor this to bean
        if(!userToEdit.getId().equals(userFromAuthToken.getId())){
            throw new ForbiddenException("You don't have the rights!");
        }
    }
}
