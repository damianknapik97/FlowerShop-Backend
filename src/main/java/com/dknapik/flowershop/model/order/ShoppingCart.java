package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.bouquet.Bouquet;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
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

    /**
     * Return all instances of <b>SINGLE</b> products inside this
     *
     * @return
     */
    public List<List<? extends ProductOrder>> getAllProductOrders() {
        List<List<? extends ProductOrder>> returnList = new LinkedList<>();

        returnList.add(occasionalArticleOrderList);
        returnList.add(souvenirOrderList);
        returnList.add(flowerOrderList);

        return returnList;
    }
}
