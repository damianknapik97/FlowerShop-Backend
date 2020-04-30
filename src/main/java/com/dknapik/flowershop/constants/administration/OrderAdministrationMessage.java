package com.dknapik.flowershop.constants.administration;

public enum OrderAdministrationMessage {
    ORDER_NOT_FOUND("Order with provided ID couldn't be found in database."),
    ORDER_UPDATED_SUCCESSFULLY("Order was updated successfully");

    private final String message;

    OrderAdministrationMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
