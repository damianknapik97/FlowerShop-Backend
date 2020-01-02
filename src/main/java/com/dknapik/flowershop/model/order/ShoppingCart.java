package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.bouquet.Bouquet;
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
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn()
    private List<OccasionalArticleOrder> occasionalArticleOrderList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn()
    private List<SouvenirOrder> souvenirOrderList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn()
    private List<FlowerOrder> flowerOrderList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn()
    private List<Bouquet> bouquetList;


    public ShoppingCart() {}

    public ShoppingCart(String name) {
        this.name = name;
    }

    public ShoppingCart(String name, List<OccasionalArticleOrder> occasionalArticleOrderList) {
        this.name = name;
        this.occasionalArticleOrderList = occasionalArticleOrderList;
    }

    public ShoppingCart(String name,
                        LocalDateTime creationDate,
                        List<OccasionalArticleOrder> occasionalArticleOrderList,
                        List<SouvenirOrder> souvenirOrderList,
                        List<FlowerOrder> flowerOrderList,
                        List<Bouquet> bouquetList) {
        this.name = name;
        this.creationDate = creationDate;
        this.occasionalArticleOrderList = occasionalArticleOrderList;
        this.souvenirOrderList = souvenirOrderList;
        this.flowerOrderList = flowerOrderList;
        this.bouquetList = bouquetList;
    }

    public ShoppingCart(String name,
                        LocalDateTime creationDate,
                        List<OccasionalArticleOrder> occasionalArticleOrderList,
                        List<SouvenirOrder> souvenirOrderList,
                        List<FlowerOrder> flowerOrderList) {
        this.name = name;
        this.creationDate = creationDate;
        this.occasionalArticleOrderList = occasionalArticleOrderList;
        this.souvenirOrderList = souvenirOrderList;
        this.flowerOrderList = flowerOrderList;
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

    public List<SouvenirOrder> getSouvenirOrderList() {
        return souvenirOrderList;
    }

    public void setSouvenirOrderList(List<SouvenirOrder> souvenirOrderList) {
        this.souvenirOrderList = souvenirOrderList;
    }

    public List<FlowerOrder> getFlowerOrderList() {
        return flowerOrderList;
    }

    public void setFlowerOrderList(List<FlowerOrder> flowerOrderList) {
        this.flowerOrderList = flowerOrderList;
    }

    public List<Bouquet> getBouquetList() {
        return bouquetList;
    }

    public void setBouquetList(List<Bouquet> bouquetList) {
        this.bouquetList = bouquetList;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", occasionalArticleOrderList=" + occasionalArticleOrderList +
                ", souvenirOrderList=" + souvenirOrderList +
                ", flowerOrderList=" + flowerOrderList +
                ", bouquetList=" + bouquetList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCart that = (ShoppingCart) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (occasionalArticleOrderList != null ? !occasionalArticleOrderList.equals(that.occasionalArticleOrderList) : that.occasionalArticleOrderList != null)
            return false;
        if (souvenirOrderList != null ? !souvenirOrderList.equals(that.souvenirOrderList) : that.souvenirOrderList != null)
            return false;
        if (flowerOrderList != null ? !flowerOrderList.equals(that.flowerOrderList) : that.flowerOrderList != null)
            return false;
        return bouquetList != null ? bouquetList.equals(that.bouquetList) : that.bouquetList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (occasionalArticleOrderList != null ? occasionalArticleOrderList.hashCode() : 0);
        result = 31 * result + (souvenirOrderList != null ? souvenirOrderList.hashCode() : 0);
        result = 31 * result + (flowerOrderList != null ? flowerOrderList.hashCode() : 0);
        result = 31 * result + (bouquetList != null ? bouquetList.hashCode() : 0);
        return result;
    }
}
