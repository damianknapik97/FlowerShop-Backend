package com.dknapik.flowershop.model.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "height", "description"})
)
@Entity
@Data
@NoArgsConstructor
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private Money price;
    @Column(length = 1024)
    private String description;
    @Column
    private int height;


    public Flower(String name, Money price, String description, int height) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.height = height;
    }
}
