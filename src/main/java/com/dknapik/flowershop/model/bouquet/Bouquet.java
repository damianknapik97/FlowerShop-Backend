package com.dknapik.flowershop.model.bouquet;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Bouquet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private String name;
    @Column
    private String productionCost;
    @Column
    private int discountPercentage;  // Is counted based on the whole price (productionCost + products costs)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private List<BouquetFlower> bouquetFlowerList;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private List<BouquetAddon> bouquetAddonList;
    @Column
    private boolean userCreated;


    public Bouquet() {
    }

    public Bouquet(String name,
                   Money productionCost,
                   int discountPercentage,
                   List<BouquetFlower> bouquetFlowerList,
                   List<BouquetAddon> bouquetAddonList,
                   boolean userCreated) {
        this.name = name;
        this.productionCost = productionCost.toString();
        this.discountPercentage = discountPercentage;
        this.bouquetFlowerList = bouquetFlowerList;
        this.bouquetAddonList = bouquetAddonList;
        this.userCreated = userCreated;
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

    public Money getProductionCost() {
        return Money.parse(productionCost);
    }

    public void setProductionCost(@NotNull Money productionCost) {
        this.productionCost = Objects.requireNonNull(productionCost).toString();
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<BouquetFlower> getFlowerOrderList() {
        return bouquetFlowerList;
    }

    public void setFlowerOrderList(List<BouquetFlower> bouquetFlowerList) {
        this.bouquetFlowerList = bouquetFlowerList;
    }

    public List<BouquetAddon> getBouquetAddonList() {
        return bouquetAddonList;
    }

    public void setBouquetAddonList(List<BouquetAddon> bouquetAddonList) {
        this.bouquetAddonList = bouquetAddonList;
    }

    public boolean isUserCreated() {
        return userCreated;
    }

    public void setUserCreated(boolean userCreated) {
        this.userCreated = userCreated;
    }

    @Override
    public String toString() {
        return "Bouquet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productionCost='" + productionCost + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", bouquetFlowerList=" + bouquetFlowerList +
                ", bouquetAddonList=" + bouquetAddonList +
                ", userCreated=" + userCreated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bouquet bouquet = (Bouquet) o;

        if (discountPercentage != bouquet.discountPercentage) return false;
        if (userCreated != bouquet.userCreated) return false;
        if (id != null ? !id.equals(bouquet.id) : bouquet.id != null) return false;
        if (name != null ? !name.equals(bouquet.name) : bouquet.name != null) return false;
        if (productionCost != null ? !productionCost.equals(bouquet.productionCost) : bouquet.productionCost != null)
            return false;
        if (bouquetFlowerList != null ? !bouquetFlowerList.equals(bouquet.bouquetFlowerList) : bouquet.bouquetFlowerList != null)
            return false;
        return bouquetAddonList != null ? bouquetAddonList.equals(bouquet.bouquetAddonList) : bouquet.bouquetAddonList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (productionCost != null ? productionCost.hashCode() : 0);
        result = 31 * result + discountPercentage;
        result = 31 * result + (bouquetFlowerList != null ? bouquetFlowerList.hashCode() : 0);
        result = 31 * result + (bouquetAddonList != null ? bouquetAddonList.hashCode() : 0);
        result = 31 * result + (userCreated ? 1 : 0);
        return result;
    }
}
