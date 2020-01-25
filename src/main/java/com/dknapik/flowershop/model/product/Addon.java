package com.dknapik.flowershop.model.product;

import com.dknapik.flowershop.utils.MoneyAmountAndCurrency;
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
        @UniqueConstraint(columnNames = {"name", "colour", "description"})
)
@TypeDefs(
        value = {
                @TypeDef(name = "moneyAmountAndCurrency", typeClass = MoneyAmountAndCurrency.class)
        }
)
@Entity
@Data
@NoArgsConstructor
public class Addon implements Product, Comparable<Addon> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddonColour colour;
    @Columns(columns = {
            @Column(name = "currency_code", nullable = false, length = 3),
            @Column(name = "price", nullable = false)
    })
    @Type(type = "moneyAmountAndCurrency")
    private MonetaryAmount price;
    @Column(length = 1024)
    private String description;


    public Addon(String name, AddonColour colour, MonetaryAmount price, String description) {
        this.name = name;
        this.colour = colour;
        this.price = price;
        this.description = description;
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
     * Compare by name
     */
    @Override
    public int compareTo(Addon o) {
        return this.name.compareTo(o.getName());
    }
}
