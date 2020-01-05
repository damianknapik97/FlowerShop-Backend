package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.product.Flower;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class FlowerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Flower flower;


    public FlowerOrder(int itemCount, Flower flower) {
        this.itemCount = itemCount;
        this.flower = flower;
    }
}
