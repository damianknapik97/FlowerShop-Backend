package com.dknapik.flowershop.model.product;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Addon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column
    private AddonColour colour;
    @Column
    private String price;
    @Column(length = 1024)
    private String description;


    public Addon() {
    }

    public Addon(String name, AddonColour colour, Money price, String description) {
        this.name = name;
        this.colour = colour;
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

    public AddonColour getColour() {
        return colour;
    }

    public void setColour(AddonColour colour) {
        this.colour = colour;
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
        return "Addon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", colour=" + colour +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Addon addon = (Addon) o;

        if (id != null ? !id.equals(addon.id) : addon.id != null) return false;
        if (name != null ? !name.equals(addon.name) : addon.name != null) return false;
        if (colour != null ? !colour.equals(addon.colour) : addon.colour != null) return false;
        if (price != null ? !price.equals(addon.price) : addon.price != null) return false;
        return description != null ? description.equals(addon.description) : addon.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
