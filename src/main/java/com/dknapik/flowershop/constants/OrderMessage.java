package com.dknapik.flowershop.constants;

public enum OrderMessage {
    /* Info */
    ORDER_UPDATED_SUCCESSFULLY("Order was updated successfully"),
    ORDER_DETAILS_UPDATED_SUCCESSFULLY("Order details were updated successfully"),
    ORDER_SUBMITTED_SUCCESSFULLY("Order was submitted successfully"),


    /* Error */
    NO_ORDERS("Your account doesn't contain any existing orders"),
    NO_ORDER_WITH_SPECIFIC_ID("Your account doesn't contain any order matching provided id"),
    ORDER_UPDATE_FAILED("Error updating order informations !"),
    NO_PRODUCTS_IN_SHOPPING_CART("No products added to shopping cart"),
    UNABLE_TO_RETRIEVE_CREATED_ORDER_ID("Saved order entity ID is unable to be retrieved"),
    NO_SHOPPING_CART_ASSIGNED("Provided order ID doesn't have any shopping cart assigned to it."),
    VALIDATION_FAILURE("Entity is not valid and therefore cannot be submitted as an Order");


    final String infoMessage;

    OrderMessage(String str) {
        infoMessage = str;
    }

    @Override
    public String toString() {
        return infoMessage;
    }
}
