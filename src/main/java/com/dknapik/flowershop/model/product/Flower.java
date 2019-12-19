package com.dknapik.flowershop.model.product;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

/**
 *
 *
 */
@Entity
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @Column
    private String price;
    @Column
    private int height;


    public Flower() { }


    public Flower(String name, Money price, int height) {
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
