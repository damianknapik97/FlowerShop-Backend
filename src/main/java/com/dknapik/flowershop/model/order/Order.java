package com.dknapik.flowershop.model.order;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_table")
public class Order {
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


    public Order() { }

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public LocalDateTime getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(LocalDateTime placementDate) {
        this.placementDate = placementDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (message != null ? !message.equals(order.message) : order.message != null) return false;
        if (deliveryDate != null ? !deliveryDate.equals(order.deliveryDate) : order.deliveryDate != null) return false;
        if (payment != null ? !payment.equals(order.payment) : order.payment != null) return false;
        if (deliveryAddress != null ? !deliveryAddress.equals(order.deliveryAddress) : order.deliveryAddress != null)
            return false;
        if (shoppingCart != null ? !shoppingCart.equals(order.shoppingCart) : order.shoppingCart != null) return false;
        return placementDate != null ? placementDate.equals(order.placementDate) : order.placementDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        result = 31 * result + (deliveryAddress != null ? deliveryAddress.hashCode() : 0);
        result = 31 * result + (shoppingCart != null ? shoppingCart.hashCode() : 0);
        result = 31 * result + (placementDate != null ? placementDate.hashCode() : 0);
        return result;
    }
}
