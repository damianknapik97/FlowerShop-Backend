package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.product.Souvenir;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class SouvenirOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Souvenir souvenir;


    public SouvenirOrder(int itemCount, Souvenir souvenir) {
        this.itemCount = itemCount;
        this.souvenir = souvenir;
    }
}
