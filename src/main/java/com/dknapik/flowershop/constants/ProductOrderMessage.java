package com.dknapik.flowershop.constants;

public enum ProductOrderMessage {
    ERROR_MATCHING_CURRENCY_UNITS("Currency units in product prices doesn't match each other");

    private final String message;

    ProductOrderMessage(String msg) {
        this.message = msg;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
