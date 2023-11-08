package com.justbelieveinmyself.marta.domain.entities;

import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(name = "order_products",joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;
    private ZonedDateTime orderedAt;
    private Boolean isPaid;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    @OneToOne
    @JoinColumn(nullable = false, name = "customer_id")
    private User customer;
}

