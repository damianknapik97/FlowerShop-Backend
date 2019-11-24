package com.dknapik.flowershop.services;

import com.dknapik.flowershop.database.ShoppingCartRepository;
import com.dknapik.flowershop.dto.ShoppingCartDto;

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
