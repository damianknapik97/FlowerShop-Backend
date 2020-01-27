package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.product.Product;
import com.dknapik.flowershop.model.product.Souvenir;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class SouvenirOrder implements ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn
    private Souvenir souvenir;


    public SouvenirOrder(int itemCount, Souvenir souvenir) {
        this.itemCount = itemCount;
        this.souvenir = souvenir;
    }

    /**
     * Retrieves original class of the product order
     *
     * @return Class instance of class implementing this interface
     */
    @Override
    @JsonIgnore
    public Class<?> getProductOrderClass() {
        return this.getClass();
    }

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - class to compare to
     * @return - true if both classes match.
     */
    @Override
    @JsonIgnore
    public boolean compareClass(Class<?> classToCompare) {
        return classToCompare.equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to ProductOrder interface
     *
     * @return Class
     */
    @Override
    @JsonIgnore
    public ProductOrder getProductOrder() {
        return this;
    }

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    @Override
    @JsonIgnore
    public Product getProduct() {
        return souvenir;
    }
}
