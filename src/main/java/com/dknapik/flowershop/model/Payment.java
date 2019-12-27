package com.dknapik.flowershop.model;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String totalPrice;
    @Column(nullable = false)
    private String paymentType;
    @Column
    private boolean wasPaid;


    public Payment() { }

    public Payment(Money totalPrice, String paymentType) {
        this.totalPrice = totalPrice.toString();
        this.paymentType = paymentType;
    }

    public Payment(Money totalPrice, String paymentType, boolean wasPaid) {
        this.totalPrice = totalPrice.toString();
        this.paymentType = paymentType;
        this.wasPaid = wasPaid;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Money getTotalPrice() {
        return Money.parse(totalPrice);
    }

    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice.toString();
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isWasPaid() {
        return wasPaid;
    }

    public void setWasPaid(boolean wasPaid) {
        this.wasPaid = wasPaid;
    }
}
