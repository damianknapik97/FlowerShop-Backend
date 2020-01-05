package com.dknapik.flowershop.model.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "description"})
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
    @Column(nullable = false)
    private Money price;
    @Column(nullable = false, length = 1024)
    private String description;


    public Souvenir(String name, Money price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
