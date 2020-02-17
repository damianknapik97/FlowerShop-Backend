package com.dknapik.flowershop.model.bouquet;

import com.dknapik.flowershop.utils.MoneyAmountAndCurrency;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@TypeDefs(
        value = {
                @TypeDef(name = "moneyAmountAndCurrency", typeClass = MoneyAmountAndCurrency.class)
        }
)
@Entity
@Data
@NoArgsConstructor
public final class Bouquet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @Columns(columns = {
            @Column(name = "currency_code", nullable = false, length = 3),
            @Column(name = "price", nullable = false)
    })
    @Type(type = "moneyAmountAndCurrency")
    private MonetaryAmount productionCost;
    @Column
    private int discountPercentage;  // Is counted based on the whole price (productionCost + products costs)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private List<BouquetFlower> bouquetFlowerList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private List<BouquetAddon> bouquetAddonList;
    @Column
    private boolean userCreated;


    public Bouquet(String name,
                   MonetaryAmount productionCost,
                   int discountPercentage,
                   List<BouquetFlower> bouquetFlowerList,
                   List<BouquetAddon> bouquetAddonList,
                   boolean userCreated) {
        this.name = name;
        this.productionCost = productionCost;
        this.discountPercentage = discountPercentage;
        this.bouquetFlowerList = bouquetFlowerList;
        this.bouquetAddonList = bouquetAddonList;
        this.userCreated = userCreated;
    }
}
