package com.dknapik.flowershop.model;

import com.dknapik.flowershop.model.product.Souvenir;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class SouvenirOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Souvenir souvenir;


    public SouvenirOrder() { }

    public SouvenirOrder(int itemCount, Souvenir souvenir) {
        this.itemCount = itemCount;
        this.souvenir = souvenir;
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

    public Souvenir getSouvenir() {
        return souvenir;
    }

    public void setSouvenir(Souvenir souvenir) {
        this.souvenir = souvenir;
    }

    @Override
    public String toString() {
        return "SouvenirOrder{" +
                "id=" + id +
                ", itemCount=" + itemCount +
                ", souvenir=" + souvenir +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SouvenirOrder that = (SouvenirOrder) o;

        if (itemCount != that.itemCount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return souvenir != null ? souvenir.equals(that.souvenir) : that.souvenir == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + itemCount;
        result = 31 * result + (souvenir != null ? souvenir.hashCode() : 0);
        return result;
    }
}
