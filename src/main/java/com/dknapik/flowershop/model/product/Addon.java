package com.dknapik.flowershop.model.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "colour", "description"})
)
@Entity
@Data
@NoArgsConstructor
public class Addon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddonColour colour;
    @Column(nullable = false)
    private Money price;
    @Column(length = 1024)
    private String description;


    public Addon(String name, AddonColour colour, Money price, String description) {
        this.name = name;
        this.colour = colour;
        this.price = price;
        this.description = description;
    }
}
