package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.product.OccasionalArticle;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class OccasionalArticleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = false)
    private OccasionalArticle occasionalArticle;


    OccasionalArticleOrder() {
    }

    public OccasionalArticleOrder(int itemCount, OccasionalArticle occasionalArticle) {
        this.itemCount = itemCount;
        this.occasionalArticle = occasionalArticle;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public OccasionalArticle getOccasionalArticle() {
        return occasionalArticle;
    }

    public void setOccasionalArticle(OccasionalArticle occasionalArticle) {
        this.occasionalArticle = occasionalArticle;
    }

    @Override
    public String toString() {
        return "OccasionalArticleOrder{" +
                "id=" + id +
                ", itemCount=" + itemCount +
                ", occasionalArticle=" + occasionalArticle +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OccasionalArticleOrder that = (OccasionalArticleOrder) o;

        if (itemCount != that.itemCount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return occasionalArticle != null ? occasionalArticle.equals(that.occasionalArticle) : that.occasionalArticle == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + itemCount;
        result = 31 * result + (occasionalArticle != null ? occasionalArticle.hashCode() : 0);
        return result;
    }
}
