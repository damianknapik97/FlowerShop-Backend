package com.dknapik.flowershop.model.bouquet;

import com.dknapik.flowershop.model.product.Flower;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class BouquetFlower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Flower flower;


    public BouquetFlower(int itemCount, Flower flower) {
        this.itemCount = itemCount;
        this.flower = flower;
    }
}
