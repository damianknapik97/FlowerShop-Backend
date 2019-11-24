package com.dknapik.flowershop.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @OneToOne()
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bouquet_id", referencedColumnName = "id")
    private List<Bouquet> bouquetList;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_pack_id", referencedColumnName = "id")
    private List<FlowerPack> flowerPackList;
    @CreatedDate
    private Date createdDate;

    public ShoppingCart(String name, Account account, List<Bouquet> bouquetList, List<FlowerPack> flowerPackList) {
        this.name = name;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        return createdDate;
    }
}
