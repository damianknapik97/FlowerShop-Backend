package com.dknapik.flowershop.utils;

import com.dknapik.flowershop.model.order.DeliveryAddress;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.Payment;
import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.stereotype.Service;

/**
 * Class used for validating Entities/Objects retrieved from database that required user input some time in the past.
 * It is possible that this c;ass is an excessive measure of security and should be highly considered
 * and questioned when it comes to performance tweaking.
 */
@Service
@ToString
public final class ValidationUtility {

    /**
     * Check if all required parameters for order to be successfully processed
     * and realized by the employees are present.
     *
     * @param order - object to check
     * @return true if all required fields for order processing are present
     */
    public boolean validateOrderEntity(@NonNull Order order) {
        return order.getId() != null
                && order.getMessage() != null
                && !order.getMessage().isEmpty()
                && order.getDeliveryDate() != null
                && order.getPayment() != null
                && order.getDeliveryAddress() != null
                && order.getShoppingCart() != null
                && order.getPlacementDate() != null
                && order.getStatus() != null;
    }

    /**
     * Check if Shopping Cart contains ID and at least one collection with product orders
     *
     * @param shoppingCart - object to check
     * @return true if object contains ID and at least one collection with product orders
     */
    public boolean validateShoppingCartEntity(@NonNull ShoppingCart shoppingCart) {
        return shoppingCart.getId() != null
                && (shoppingCart.getOccasionalArticleOrderList() != null
                        || shoppingCart.getSouvenirOrderList() != null
                        || shoppingCart.getFlowerOrderList() != null
                        || shoppingCart.getBouquetList() != null);
    }

    /**
     * Checks if all field in Payment object required for payment processing are present.
     *
     * @param payment - Object to check
     * @return true if id, total price and payment type are present inside provided object
     */
    public boolean validatePaymentEntity(@NonNull Payment payment) {
        return payment.getId() != null
                && payment.getTotalPrice() != null
                && payment.getTotalPrice().isPositive()
                && payment.getPaymentType() != null;
    }


    /**
     * Check if all required fields inside DeliveryAddress object for successful order delivery
     * are present and not empty.
     *
     * @param deliveryAddress - Object to check
     * @return true if all required field for successful order delivery are present and not empty.
     */
    public boolean validateDeliveryAddressEntity(@NonNull DeliveryAddress deliveryAddress) {
        return deliveryAddress.getId() != null
                && deliveryAddress.getCityName() != null && !deliveryAddress.getCityName().isEmpty()
                && deliveryAddress.getZipCode() != null && !deliveryAddress.getZipCode().isEmpty()
                && deliveryAddress.getStreetName() != null && !deliveryAddress.getStreetName().isEmpty()
                && deliveryAddress.getHouseNumber() != null && !deliveryAddress.getHouseNumber().isEmpty();
    }
}
