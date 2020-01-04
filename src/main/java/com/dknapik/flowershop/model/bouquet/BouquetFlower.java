package com.dknapik.flowershop.model.bouquet;

import com.dknapik.flowershop.model.product.Flower;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class BouquetFlower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Flower flower;


    public BouquetFlower() {
    }

    public BouquetFlower(int itemCount, Flower flower) {
        this.itemCount = itemCount;
        this.flower = flower;
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

    public Flower getFlower() {
        return flower;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }

    @Override
    public String toString() {
        return "BouquetFlower{" +
                "id=" + id +
                ", itemCount=" + itemCount +
                ", flower=" + flower +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BouquetFlower that = (BouquetFlower) o;

        if (itemCount != that.itemCount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return flower != null ? flower.equals(that.flower) : that.flower == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + itemCount;
        result = 31 * result + (flower != null ? flower.hashCode() : 0);
        return result;
    }
}
