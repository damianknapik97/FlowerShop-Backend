package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.Model;
import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
import com.dknapik.flowershop.model.productorder.ProductOrder;
import com.dknapik.flowershop.model.productorder.SouvenirOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public final class ShoppingCart implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @CreatedDate
    @Column
    private LocalDateTime creationDate;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn()
    private List<OccasionalArticleOrder> occasionalArticleOrderList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn()
    private List<SouvenirOrder> souvenirOrderList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn()
    private List<FlowerOrder> flowerOrderList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn()
    private List<Bouquet> bouquetList;


    public ShoppingCart(String name) {
        this.name = name;
    }

    public ShoppingCart(String name, List<OccasionalArticleOrder> occasionalArticleOrderList) {
        this.name = name;
        this.occasionalArticleOrderList = occasionalArticleOrderList;
    }

    public ShoppingCart(String name,
                        List<OccasionalArticleOrder> occasionalArticleOrderList,
                        List<SouvenirOrder> souvenirOrderList,
                        List<FlowerOrder> flowerOrderList,
                        List<Bouquet> bouquetList) {
        this.name = name;
        this.occasionalArticleOrderList = occasionalArticleOrderList;
        this.souvenirOrderList = souvenirOrderList;
        this.flowerOrderList = flowerOrderList;
        this.bouquetList = bouquetList;
    }

    /**
     * Return all instances of <b>SINGLE</b> products inside this
     *
     * @return
     */
    @JsonIgnore
    public List<List<? extends ProductOrder>> getAllProductOrders() {
        List<List<? extends ProductOrder>> returnList = new LinkedList<>();

        returnList.add(occasionalArticleOrderList);
        returnList.add(souvenirOrderList);
        returnList.add(flowerOrderList);

        return returnList;
    }
}
