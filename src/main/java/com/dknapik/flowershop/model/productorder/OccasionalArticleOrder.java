package com.dknapik.flowershop.model.productorder;

import com.dknapik.flowershop.model.Model;
import com.dknapik.flowershop.model.product.OccasionalArticle;
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
public final class OccasionalArticleOrder implements Model, ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn()
    private OccasionalArticle occasionalArticle;


    public OccasionalArticleOrder(int itemCount, OccasionalArticle occasionalArticle) {
        this.itemCount = itemCount;
        this.occasionalArticle = occasionalArticle;
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
        return occasionalArticle;
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
        if (product.compareClass(OccasionalArticle.class)) {
            occasionalArticle = (OccasionalArticle) product;
        } else {
            throw new IllegalArgumentException("Provided product dto class - " +
                    product.getProductClass().toString() +
                    " - doesn't match the one inside product order dto -" +
                    OccasionalArticle.class.toString());
        }
    }
}