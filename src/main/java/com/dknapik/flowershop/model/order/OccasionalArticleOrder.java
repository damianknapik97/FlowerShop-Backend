package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.product.OccasionalArticle;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class OccasionalArticleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = false)
    private OccasionalArticle occasionalArticle;


    public OccasionalArticleOrder(int itemCount, OccasionalArticle occasionalArticle) {
        this.itemCount = itemCount;
        this.occasionalArticle = occasionalArticle;
    }
}
