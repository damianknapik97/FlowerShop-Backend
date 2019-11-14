package com.dknapik.flowershop.model;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bouquet_id", referencedColumnName = "id")
    private Set<Bouquet> bouquetSet;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "flower_pack_id", referencedColumnName = "id")
    private Set<FlowerPack> flowerPackSet;

    public ShoppingCart(String name, Set<Bouquet> bouquetSet, Set<FlowerPack> flowerPackSet) {
        this.name = name;
        this.bouquetSet = bouquetSet;
        this.flowerPackSet = flowerPackSet;
    }

    public ShoppingCart() { }

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

    public Set<Bouquet> getBouquetSet() {
        return bouquetSet;
    }

    public void setBouquetSet(Set<Bouquet> bouquetSet) {
        this.bouquetSet = bouquetSet;
    }

    public Set<FlowerPack> getFlowerPackSet() {
        return flowerPackSet;
    }

    public void setFlowerPackSet(Set<FlowerPack> flowerPackSet) {
        this.flowerPackSet = flowerPackSet;
    }
}
