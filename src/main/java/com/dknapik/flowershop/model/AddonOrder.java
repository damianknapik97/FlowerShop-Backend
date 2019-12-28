package com.dknapik.flowershop.model;

import com.dknapik.flowershop.model.product.Addon;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class AddonOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Addon addon;


    public AddonOrder() { }

    public AddonOrder(Addon addon) {
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
        return "AddonOrder{" +
                "id=" + id +
                ", addon=" + addon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddonOrder that = (AddonOrder) o;

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
