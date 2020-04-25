package com.dknapik.flowershop.constants;

public enum PaymentMessage {
    CREATED_SUCCESSFULLY("Payment created and assigned to your order successfully."),
    ALREADY_ASSIGNED("Couldn't assign payment because it is already assigned."),
    UPDATE_FAILED("Couldn't update provided payment entity because it doesn't exist in database.");

    private final String message;

    PaymentMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return this.message;
    }
}
