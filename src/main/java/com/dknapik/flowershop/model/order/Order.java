package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_table")
@Data
@NoArgsConstructor
public final class Order implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(length = 50)
    private String message;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deliveryDate;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private Payment payment;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private DeliveryAddress deliveryAddress;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REMOVE})
    @JoinColumn
    private ShoppingCart shoppingCart;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime placementDate;
    @Column(length = 1024)
    private String additionalNote;
    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status = OrderStatus.CREATED;


    public Order(String message,
                 Payment payment,
                 DeliveryAddress deliveryAddress,
                 ShoppingCart shoppingCart) {
        this.message = message;
        this.payment = payment;
        this.deliveryAddress = deliveryAddress;
        this.shoppingCart = shoppingCart;
    }

    public Order(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
