package com.dknapik.flowershop.constants;

public enum DeliveryAddressMessage {
    ADDED_SUCCESSFULLY("Delivery address was added successfully to your order"),
    ALREADY_ASSIGNED("Couldn't assign delivery address because it is already assigned"),
    UPDATE_FAILED("Couldn't update provided delivery address entity because it doesn't exist in database.");


    final String infoMessage;

    DeliveryAddressMessage(String str) {
        infoMessage = str;
    }

    @Override
    public String toString() {
        return infoMessage;
    }
}
