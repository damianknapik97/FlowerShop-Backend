package com.dknapik.flowershop.model.product;

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

@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "description"})
)
@TypeDefs(
        value = {
                @TypeDef(name = "moneyAmountAndCurrency", typeClass = MoneyAmountAndCurrency.class)
        }
)
@Entity
@Data
@NoArgsConstructor
public class Souvenir {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    @Columns(columns = {
            @Column(name = "currency_code", nullable = false, length = 3),
            @Column(name = "price", nullable = false)
    })
    @Type(type = "moneyAmountAndCurrency")
    private MonetaryAmount price;
    @Column(nullable = false, length = 1024)
    private String description;


    public Souvenir(String name, MonetaryAmount price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
