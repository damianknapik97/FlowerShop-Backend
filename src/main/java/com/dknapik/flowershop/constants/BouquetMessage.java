package com.dknapik.flowershop.constants;

public enum BouquetMessage {
    CURRENCIES_NOT_MATCHING("Error, currencies doesn't match each other.");

    final String message;

    BouquetMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
