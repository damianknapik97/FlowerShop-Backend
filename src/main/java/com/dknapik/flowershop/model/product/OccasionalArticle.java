package com.dknapik.flowershop.model.product;

import com.dknapik.flowershop.utils.MoneyAmountAndCurrency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.util.UUID;

@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "description", "theme"})
)
@TypeDefs(
        value = {
                @TypeDef(name = "moneyAmountAndCurrency", typeClass = MoneyAmountAndCurrency.class)
        }
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccasionalArticle implements Product, Comparable<OccasionalArticle> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Columns(columns = {
            @Column(name = "currency_code", nullable = false, length = 3),
            @Column(name = "price", nullable = false)
    })
    @Type(type = "moneyAmountAndCurrency")
    private MonetaryAmount price;
    @Column(nullable = false, length = 1024)
    private String description;
    @Column
    private String theme;


    public OccasionalArticle(String name, MonetaryAmount price, String description, String theme) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.theme = theme;
    }

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    @Override
    @JsonIgnore
    public Class<?> getProductClass() {
        return this.getClass();
    }

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare- Object to compare to
     * @return - true if both classes match.
     */
    @Override
    @JsonIgnore
    public boolean compareClass(Class<?> classToCompare) {
        return classToCompare.equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to Product interface
     *
     * @return Class
     */
    @Override
    @JsonIgnore
    public Product getProduct() {
        return this;
    }

    /**
     * Compares by name
     */
    @Override
    @JsonIgnore
    public int compareTo(OccasionalArticle o) {
        return this.name.compareTo(o.getName());
    }
}
