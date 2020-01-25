package com.dknapik.flowershop.model.product;

import com.dknapik.flowershop.utils.MoneyAmountAndCurrency;
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
public class Flower implements Product, Comparable<Flower> {
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


    public Flower(String name, MonetaryAmount price, String description, int height) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.height = height;
    }

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    @Override
    public Class<?> getProductClass() {
        return this.getClass();
    }

    /**
     * Compare given product original class with this products class.
     *
     * @param product - Object to compare to
     * @return - true if both classes match.
     */
    @Override
    public boolean compareClass(Product product) {
        return product.getProductClass().equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to Product interface
     *
     * @return Class
     */
    @Override
    public Product getProduct() {
        return this;
    }

    /**
     * Sort by name
     */
    @Override
    public int compareTo(Flower o) {
        return this.name.compareTo(o.name);
    }
}
