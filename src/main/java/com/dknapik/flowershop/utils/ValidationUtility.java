package com.dknapik.flowershop.utils;

import com.dknapik.flowershop.model.order.DeliveryAddress;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.Payment;
import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Class used for validating Entities/Objects retrieved from database that required user input some time in the past.
 * It is possible that this c;ass is an excessive measure of security and should be highly considered
 * and questioned when it comes to performance tweaking.
 */
@Component
@ToString
@Log4j2
public final class ValidationUtility {

    /**
     * Check if all required parameters for order to be successfully processed
     * and realized by the employees are present.
     *
     * @param order - object to check
     * @return true if all required fields for order processing are present
     */
    public boolean validateOrderEntity(@NonNull Order order) {
        log.traceEntry();
        boolean returnValue = order.getId() != null
                && order.getMessage() != null
                && !order.getMessage().isEmpty()
                && order.getDeliveryDate() != null
                && order.getPayment() != null
                && order.getDeliveryAddress() != null
                && order.getShoppingCart() != null
                && order.getPlacementDate() != null
                && order.getStatus() != null;
        log.info(() -> "Order validation results: " + returnValue);

        log.traceExit();
        return returnValue;
    }

    /**
     * Check if Shopping Cart contains ID and at least one collection with product orders
     *
     * @param shoppingCart - object to check
     * @return true if object contains ID and at least one collection with product orders
     */
    public boolean validateShoppingCartEntity(@NonNull ShoppingCart shoppingCart) {
        log.traceEntry();
        boolean returnValue = shoppingCart.getId() != null
                && (shoppingCart.getOccasionalArticleOrderList() != null
                || shoppingCart.getSouvenirOrderList() != null
                || shoppingCart.getFlowerOrderList() != null
                || shoppingCart.getBouquetList() != null);

        log.info(() -> "Shopping cart validation results: " + returnValue);

        log.traceExit();
        return returnValue;
    }

    /**
     * Checks if all field in Payment object required for payment processing are present.
     *
     * @param payment - Object to check
     * @return true if id, total price and payment type are present inside provided object
     */
    public boolean validatePaymentEntity(@NonNull Payment payment) {
        log.traceEntry();

        boolean returnValue = payment.getId() != null
                && payment.getTotalPrice() != null
                && payment.getTotalPrice().isPositive()
                && payment.getPaymentType() != null;
        log.info(() -> "Payment validation results: " + returnValue);

        log.traceExit();
        return returnValue;
    }


    /**
     * Check if all required fields inside DeliveryAddress object for successful order delivery
     * are present and not empty.
     *
     * @param deliveryAddress - Object to check
     * @return true if all required field for successful order delivery are present and not empty.
     */
    public boolean validateDeliveryAddressEntity(@NonNull DeliveryAddress deliveryAddress) {
        log.traceEntry();

        boolean returnValue = deliveryAddress.getId() != null
                && deliveryAddress.getCityName() != null && !deliveryAddress.getCityName().isEmpty()
                && deliveryAddress.getZipCode() != null && !deliveryAddress.getZipCode().isEmpty()
                && deliveryAddress.getStreetName() != null && !deliveryAddress.getStreetName().isEmpty()
                && deliveryAddress.getHouseNumber() != null && !deliveryAddress.getHouseNumber().isEmpty();
        log.info(() -> "Delivery Address validation results: " + returnValue);

        log.traceExit();
        return returnValue;
    }
}
