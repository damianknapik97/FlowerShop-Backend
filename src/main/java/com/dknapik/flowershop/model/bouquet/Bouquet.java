package com.dknapik.flowershop.model.bouquet;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Bouquet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @Column
    private Money productionCost;
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
                   Money productionCost,
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
