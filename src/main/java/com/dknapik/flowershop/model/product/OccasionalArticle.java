package com.dknapik.flowershop.model.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.UUID;

@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"name", "description", "theme"})
)
@Entity
@Data
@NoArgsConstructor
public class OccasionalArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Money price;
    @Column(nullable = false, length = 1024)
    private String description;
    @Column
    private String theme;


    public OccasionalArticle(String name, Money price, String description, String theme) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.theme = theme;
    }
}
