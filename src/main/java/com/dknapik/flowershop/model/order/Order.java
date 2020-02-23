package com.dknapik.flowershop.model.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_table")
@Data
@NoArgsConstructor
public final class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String message;
    @Column
    private LocalDateTime deliveryDate;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private Payment payment;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private DeliveryAddress deliveryAddress;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private ShoppingCart shoppingCart;
    @CreatedDate
    @Column
    private LocalDateTime placementDate;
    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status = OrderStatus.CREATED;


    public Order(String message,
                 LocalDateTime deliveryDate,
                 Payment payment,
                 DeliveryAddress deliveryAddress,
                 ShoppingCart shoppingCart) {
        this.message = message;
        this.deliveryDate = deliveryDate;
        this.payment = payment;
        this.deliveryAddress = deliveryAddress;
        this.shoppingCart = shoppingCart;
    }

    public Order(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
