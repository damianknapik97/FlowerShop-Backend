package com.dknapik.flowershop.model.bouquet;

import com.dknapik.flowershop.model.product.Addon;
import com.dknapik.flowershop.model.product.Product;
import com.dknapik.flowershop.model.productorder.ProductOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public final class BouquetAddon implements ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn()
    private Addon addon;

    public BouquetAddon(Addon addon) {
        this.addon = addon;
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
        return addon;
    }

    /**
     * Checks if provided Product is able to be casted to Product class inside this Order and casts it.
     *
     * @param product - Product DTO to set inside this Order DTO class
     */
    @Override
    @JsonIgnore
    public void setProduct(Product product) {
        Objects.requireNonNull(product);
        if (product.compareClass(Addon.class)) {
            addon = (Addon) product;
        } else {
            throw new IllegalArgumentException("Provided product dto class - " +
                    product.getProductClass().toString() +
                    " - doesn't match the one inside product order dto -" +
                    Addon.class.toString());
        }
    }

    /**
     * WARNING ! This method returns always 1 because Addon entities doesn't include number of products.
     * Reason behind this solution is that addons can not be measured in units as different bouquets require
     * different amount of them based on the florist preferences/skills. The intention to give possible customers
     * option to customize bouquets based on their preferences enforces this idea as clients are unable to determine
     * how much, for example blue ribbon would their product require in the end.
     *
     * @return integer that is always 1
     */
    @Override
    @JsonIgnore
    public int getOrderedAmount() {
        return 1;
    }
}
