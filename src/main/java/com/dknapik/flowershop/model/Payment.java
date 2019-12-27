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
    @Enumerated(EnumType.STRING)
    @Column
    private PaymentType paymentType;
    @Column
    private boolean wasPaid;


    public Payment() { }

    public Payment(Money totalPrice, PaymentType paymentType) {
        this.totalPrice = totalPrice.toString();
        this.paymentType = paymentType;
    }

    public Payment(Money totalPrice, PaymentType paymentType, boolean wasPaid) {
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

    public boolean isWasPaid() {
        return wasPaid;
    }

    public void setWasPaid(boolean wasPaid) {
        this.wasPaid = wasPaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        if (wasPaid != payment.wasPaid) return false;
        if (id != null ? !id.equals(payment.id) : payment.id != null) return false;
        if (totalPrice != null ? !totalPrice.equals(payment.totalPrice) : payment.totalPrice != null) return false;
        return paymentType != null ? paymentType.equals(payment.paymentType) : payment.paymentType == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        result = 31 * result + (paymentType != null ? paymentType.hashCode() : 0);
        result = 31 * result + (wasPaid ? 1 : 0);
        return result;
    }
}
