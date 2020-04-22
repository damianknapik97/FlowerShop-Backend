package com.dknapik.flowershop.constants.administration;

public enum AccountAdministrationMessage {
    ACCOUNT_UPDATED_SUCCESSFULLY("Account was updated successfully."),
    ACCOUNT_NOT_FOUND("Account with provided ID couldn't be found in database.");

    private final String message;

    AccountAdministrationMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
