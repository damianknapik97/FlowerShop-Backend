package com.dknapik.flowershop.api;

import com.dknapik.flowershop.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;

public class ShoppingCartController {
    private final ShoppingCartService service;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.service = shoppingCartService;
    }

}
