package com.dknapik.flowershop.constants;

public enum ShoppingCartMessage {
    PRODUCT_ADDED_SUCCESSFULLY("Added product to your shopping cart"),
    PRODUCT_NOT_ADDED("Failed to add product to your shopping cart");

    final String infoMessage;

    ShoppingCartMessage(String str) {
        infoMessage = str;
    }

    @Override
    public String toString() {
        return infoMessage;
    }
}
