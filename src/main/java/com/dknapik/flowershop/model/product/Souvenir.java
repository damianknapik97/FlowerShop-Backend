package com.dknapik.flowershop.model.product;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Souvenir {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private String name;
    @Column
    private String price;
    @Column
    private String description;

    public Souvenir() {}

    public Souvenir(String name, Money price, String description) {
        this.name = name;
        this.price = price.toString();
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getPrice() {
        return Money.parse(price);
    }

    public void setPrice(Money price) {
        this.price = price.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
