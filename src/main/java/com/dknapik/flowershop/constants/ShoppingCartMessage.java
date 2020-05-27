package com.dknapik.flowershop.constants;

public enum ShoppingCartMessage {
    PRODUCT_ADDED_SUCCESSFULLY("Added product to your shopping cart"),

    PRODUCT_REMOVED_SUCCESSFULLY("Removed product from your shopping cart"),

    ERROR_MATCHING_CURRENCY_UNITS("Currency units between product categories doesn't match each other"),
    SHOPPING_CART_NOT_FOUND("No shopping cart matches provided ID");

    final String infoMessage;

    ShoppingCartMessage(String str) {
        infoMessage = str;
    }

    @Override
    public String toString() {
        return infoMessage;
    }
}
