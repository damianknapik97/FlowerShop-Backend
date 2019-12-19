package com.dknapik.flowershop.services;

import com.dknapik.flowershop.database.ShoppingCartRepository;

public class ShoppingCartService {
    private final ShoppingCartRepository repository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.repository = shoppingCartRepository;
    }

    public ShoppingCartDto getShoppingList() {
        ShoppingCartDto toReturn = null;

        return toReturn;
    }


}
