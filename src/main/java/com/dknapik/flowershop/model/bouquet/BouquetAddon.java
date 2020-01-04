package com.dknapik.flowershop.model.bouquet;

import com.dknapik.flowershop.model.product.Addon;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class BouquetAddon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Addon addon;


    public BouquetAddon() {
    }

    public BouquetAddon(Addon addon) {
        this.addon = addon;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }

    @Override
    public String toString() {
        return "BouquetAddon{" +
                "id=" + id +
                ", addon=" + addon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BouquetAddon that = (BouquetAddon) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return addon != null ? addon.equals(that.addon) : that.addon == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (addon != null ? addon.hashCode() : 0);
        return result;
    }
}
