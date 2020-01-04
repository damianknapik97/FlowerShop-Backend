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
    @Column(length = 1024)
    private String description;


    public Souvenir() {
    }

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

    @Override
    public String toString() {
        return "Souvenir{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Souvenir souvenir = (Souvenir) o;

        if (id != null ? !id.equals(souvenir.id) : souvenir.id != null) return false;
        if (name != null ? !name.equals(souvenir.name) : souvenir.name != null) return false;
        if (price != null ? !price.equals(souvenir.price) : souvenir.price != null) return false;
        return description != null ? description.equals(souvenir.description) : souvenir.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
