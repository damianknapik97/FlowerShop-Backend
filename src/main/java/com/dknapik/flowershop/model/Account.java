package com.dknapik.flowershop.model;

import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Represents accounts in application
 *
 * @author Damian
 */
@Entity
@Data
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role = AccountRole.USER;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn
    private ShoppingCart shoppingCart = new ShoppingCart();
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn
    private List<Order> orderList;


    public Account(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public Account(String password, String email, AccountRole role) {
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Account(String name, String password, String email, AccountRole role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Account(String name,
                   String password,
                   String email,
                   AccountRole role,
                   ShoppingCart shoppingCart,
                   List<Order> orderList) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.shoppingCart = shoppingCart;
        this.orderList = orderList;
    }


}
