package com.dknapik.flowershop.model.bouquet;

import com.dknapik.flowershop.model.product.Addon;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class BouquetAddon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Addon addon;


    public BouquetAddon(Addon addon) {
        this.addon = addon;
    }
}
