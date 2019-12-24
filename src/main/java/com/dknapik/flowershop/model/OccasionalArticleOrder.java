package com.dknapik.flowershop.model;

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
    @JoinColumn()
    private OccasionalArticle occasionalArticle;


    OccasionalArticleOrder() {}

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
}
