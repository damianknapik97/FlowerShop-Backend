package com.dknapik.flowershop.constants.administration;

public enum AccountAdministrationMessage {
    UPDATED_SUCCESSFULLY("Account was updated successfully."),
    NOT_FOUND_BY_ORDER_ID("No account could be linked to provided order id"),
    NOT_FOUND("Account with provided ID couldn't be found in database.");


    private final String message;

    AccountAdministrationMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
