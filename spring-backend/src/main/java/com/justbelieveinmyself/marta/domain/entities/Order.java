package com.justbelieveinmyself.marta.domain.entities;

import com.justbelieveinmyself.marta.domain.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(exclude = {"orderProduct"})
@ToString(exclude = {"orderProduct"})
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProduct;
    @CreationTimestamp
    private ZonedDateTime orderedAt;
    private Boolean isPaid;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "customer_id")
    private User customer;
}

