package com.dknapik.flowershop.constants;

public enum PaymentMessage {
    PAYMENT_CREATED_SUCCESSFULLY("Payment created and assigned to your order successfully"),
    PAYMENT_ALREADY_ASSIGNED("Couldn't assign payment because it is already assigned");

    private final String message;

    PaymentMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return this.message;
    }
}
