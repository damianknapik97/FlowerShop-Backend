package com.dknapik.flowershop.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @CreatedDate
    @Column
    private Date creationDate;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn()
    private List<OccasionalArticleOrder> occasionalArticleOrderList;


    public ShoppingCart() {}

    public ShoppingCart(String name) {
        this.name = name;
    }

    public ShoppingCart(String name, List<OccasionalArticleOrder> occasionalArticleOrderList) {
        this.name = name;
        this.occasionalArticleOrderList = occasionalArticleOrderList;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<OccasionalArticleOrder> getOccasionalArticleOrderList() {
        return occasionalArticleOrderList;
    }

    public void setOccasionalArticleOrderList(List<OccasionalArticleOrder> occasionalArticleOrderList) {
        this.occasionalArticleOrderList = occasionalArticleOrderList;
    }
}
