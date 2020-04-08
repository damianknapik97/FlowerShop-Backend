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
        @UniqueConstraint(columnNames = {"name", "height", "description"})
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
public final class Flower implements Product, Comparable<Flower> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    @Columns(columns = {
            @Column(name = "currency_code", nullable = false, length = 3),
            @Column(name = "price", nullable = false)
    })
    @Type(type = "moneyAmountAndCurrency")
    private MonetaryAmount price;
    @Column(length = 1024)
    private String description;
    @Column
    private int height;
    @Column
    private String imageLarge;
    @Column
    private String imageMedium;
    @Column
    private String imageSmall;

    public Flower(String name, MonetaryAmount price, String description, int height) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.height = height;
    }

    public Flower(String name, MonetaryAmount price, String description, int height,
                  String imageLarge, String imageMedium, String imageSmall) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.height = height;
        this.imageLarge = imageLarge;
        this.imageMedium = imageMedium;
        this.imageSmall = imageSmall;
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
     * @param classToCompare - Object to compare to
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
     * Sort by name
     */
    @Override
    @JsonIgnore
    public int compareTo(Flower o) {
        return this.name.compareTo(o.name);
    }
}
