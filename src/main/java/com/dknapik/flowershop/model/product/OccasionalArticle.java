package com.dknapik.flowershop.model.product;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Table(
        uniqueConstraints =
                @UniqueConstraint(columnNames = {"name", "description", "theme"})
)
@Entity
public class OccasionalArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @Column
    private String price;
    @Column
    private String description;
    @Column
    private String theme;


    public OccasionalArticle() { }

    public OccasionalArticle(String name, Money price, String description, String theme) {
        this.name = name;
        this.price = price.toString();
        this.description = description;
        this.theme = theme;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
