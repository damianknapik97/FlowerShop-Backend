package com.dknapik.flowershop.constants;

public enum ProductMessage {
    PRODUCT_NOT_FOUND("The specified product cannot be found in the database");

    private final String message;

    ProductMessage(String msg) {
        this.message = msg;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
