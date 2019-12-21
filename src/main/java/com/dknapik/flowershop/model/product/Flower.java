package com.dknapik.flowershop.model.product;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;


@Entity
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private String name;
    @Column
    private String price;
    @Column
    private String description;
    @Column
    private int height;

    public Flower() { }

    public Flower(String name, Money price, String description, int height) {
        this.name = name;
        this.price = price.toString();
        this.height = height;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Flower flower = (Flower) o;
        return height == flower.height &&
                name.equals(flower.name) &&
                description.equals(flower.description) &&
                price.equals(flower.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, description, height);
    }
}
