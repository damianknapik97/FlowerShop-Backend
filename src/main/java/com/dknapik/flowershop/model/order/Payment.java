package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.Model;
import com.dknapik.flowershop.utils.MoneyAmountAndCurrency;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.util.UUID;

@TypeDefs(
        value = {
                @TypeDef(name = "moneyAmountAndCurrency", typeClass = MoneyAmountAndCurrency.class)
        }
)
@Entity
@Data
@NoArgsConstructor
public final class Payment implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Columns(columns = {
            @Column(name = "currency_code", nullable = false, length = 3),
            @Column(name = "price", nullable = false)
    })
    @Type(type = "moneyAmountAndCurrency")
    private MonetaryAmount totalPrice;
    @Enumerated(EnumType.STRING)
    @Column
    private PaymentType paymentType;
    @Column
    private boolean wasPaid;


    public Payment(MonetaryAmount totalPrice, PaymentType paymentType) {
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
    }

}
