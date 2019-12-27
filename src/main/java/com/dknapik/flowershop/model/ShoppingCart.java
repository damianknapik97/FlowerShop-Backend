package com.dknapik.flowershop.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime creationDate;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<OccasionalArticleOrder> getOccasionalArticleOrderList() {
        return occasionalArticleOrderList;
    }

    public void setOccasionalArticleOrderList(List<OccasionalArticleOrder> occasionalArticleOrderList) {
        this.occasionalArticleOrderList = occasionalArticleOrderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCart that = (ShoppingCart) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        return occasionalArticleOrderList != null ? occasionalArticleOrderList.equals(that.occasionalArticleOrderList) : that.occasionalArticleOrderList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (occasionalArticleOrderList != null ? occasionalArticleOrderList.hashCode() : 0);
        return result;
    }
}
