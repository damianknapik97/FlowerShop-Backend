package com.dknapik.flowershop.constants;

public enum DeliveryAddressMessage {
    DELIVERY_ADDRESS_ADDED_SUCCESSFULLY("Delivery address was added successfully to your order"),
    DELIVERY_ADDRESS_ALREADY_ASSIGNED("Couldn't assign delivery address because it is already assigned");

    final String infoMessage;

    DeliveryAddressMessage(String str) {
        infoMessage = str;
    }

    @Override
    public String toString() {
        return infoMessage;
    }
}
