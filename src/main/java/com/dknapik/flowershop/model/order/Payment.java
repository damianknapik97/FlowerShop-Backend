package com.dknapik.flowershop.model.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private Money totalPrice;
    @Enumerated(EnumType.STRING)
    @Column
    private PaymentType paymentType;
    @Column
    private boolean wasPaid;


    public Payment(Money totalPrice, PaymentType paymentType) {
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
    }

    public Payment(Money totalPrice, PaymentType paymentType, boolean wasPaid) {
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
        this.wasPaid = wasPaid;
    }
}
