package com.dknapik.flowershop.model.order;

import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.model.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class OccasionalArticleOrder implements ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private int itemCount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = false)
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
    public Class<?> getProductOrderClass() {
        return this.getClass();
    }

    /**
     * Compare given product original class with this products class.
     *
     * @param productOrder - Object to compare to
     * @return - true if both classes match.
     */
    @Override
    public boolean compareClass(ProductOrder productOrder) {
        return productOrder.getClass().equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to ProductOrder interface
     *
     * @return Class
     */
    @Override
    public ProductOrder getProductOrder() {
        return this;
    }

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    @Override
    public Product getProduct() {
        return occasionalArticle;
    }
}
