package com.dknapik.flowershop.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
    private List<Bouquet> bouquetList;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
    private List<FlowerPack> flowerPackList;
    @CreatedDate
    private Date creationDate;

    public ShoppingCart(String name, List<Bouquet> bouquetList, List<FlowerPack> flowerPackList) {
        this.name = name;
        this.bouquetList = bouquetList;
        this.flowerPackList = flowerPackList;
    }

    public ShoppingCart() {}

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

    public List<Bouquet> getBouquetList() {
        return bouquetList;
    }

    public void setBouquetList(List<Bouquet> bouquetList) {
        this.bouquetList = bouquetList;
    }

    public List<FlowerPack> getFlowerPackList() {
        return flowerPackList;
    }

    public void setFlowerPackList(List<FlowerPack> flowerPackList) {
        this.flowerPackList = flowerPackList;
    }

    public Date getCreatedDate() {
        return creationDate;
    }
}
