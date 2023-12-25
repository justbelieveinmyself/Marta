package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.OrderDto;
import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.entities.Order;
import com.justbelieveinmyself.marta.domain.entities.OrderProduct;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import com.justbelieveinmyself.marta.repositories.OrderRepository;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private ProductRepository productRepository;
    @Test
    void getListOrders() {
        List<Order> orders = new ArrayList<>();
        Product product = Product.builder().id(3L).productName("Testname").build();
        Order order = Order.builder().id(2L).orderedAt(ZonedDateTime.now()).build();
        OrderProduct orderProduct = OrderProduct.builder().order(order).product(product).quantity(3).build();
        order.setOrderProduct(new HashSet<>(Arrays.asList(orderProduct)));
        orders.add(order);
        User mockUser = User.builder().id(1L).orders(orders).build();
        order.setCustomer(mockUser);

        ResponseEntity<List<OrderDto>> ordersAsResponseEntity = orderService.getListOrders(mockUser);

        assertEquals(2L, ordersAsResponseEntity.getBody().get(0).getId());
        assertTrue(ordersAsResponseEntity.getBody().get(0).getProductIdAndQuantity().containsKey(3L));
        assertEquals(1L, ordersAsResponseEntity.getBody().get(0).getCustomer().getId());
    }

    @Test
    void createOrder() {
        SellerDto mockSellerDto = SellerDto.builder().id(5L).build();
        Map<Long, Integer> productIdAndQuantity = new HashMap<>();
        productIdAndQuantity.put(4L, 2);
        productIdAndQuantity.put(2L, 1);
        OrderDto orderDto = OrderDto.builder().id(1L).orderedAt(ZonedDateTime.now()).status(DeliveryStatus.SENT).customer(mockSellerDto).isPaid(false).productIdAndQuantity(productIdAndQuantity).build();
        User mockUser = User.builder().id(1L).orders(new ArrayList<>()).build();

        when(productRepository.findById(anyLong())).thenAnswer(i -> Optional.of(Product.builder().id((Long) i.getArguments()[0]).build()));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<OrderDto> orderDtoAsResponseEntity = orderService.createOrder(mockUser, orderDto);

        assertEquals(DeliveryStatus.AWAITING_CONFIRMATION, orderDtoAsResponseEntity.getBody().getStatus());
        assertEquals(2, orderDtoAsResponseEntity.getBody().getProductIdAndQuantity().size());
        assertTrue(orderDtoAsResponseEntity.getBody().getProductIdAndQuantity().containsKey(4L));
        assertTrue(orderDtoAsResponseEntity.getBody().getProductIdAndQuantity().containsKey(2L));
        assertTrue(orderDtoAsResponseEntity.getBody().getProductIdAndQuantity().containsValue(1));
        assertTrue(orderDtoAsResponseEntity.getBody().getProductIdAndQuantity().containsValue(2));
        assertFalse(orderDtoAsResponseEntity.getBody().getIsPaid());
        assertEquals(1L, orderDtoAsResponseEntity.getBody().getCustomer().getId());

        verify(productRepository, times(2)).findById(anyLong());
    }

    @Test
    void changeOrderStatus() {
    }
}