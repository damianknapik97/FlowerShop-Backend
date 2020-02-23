package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.DeliveryAddressMessage;
import com.dknapik.flowershop.database.order.DeliveryAddressRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.order.DeliveryAddress;
import com.dknapik.flowershop.model.order.Order;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@ToString
@Log4j2
@Service
public final class DeliveryAddressService {
    private final DeliveryAddressRepository repository;
    private final OrderService orderService;


    @Autowired
    public DeliveryAddressService(DeliveryAddressRepository repository,
                                  OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;
    }

    /**
     * Search for provided Order ID inside provided account,
     * check if delivery address is already assigned to this order,
     * save the provided delivery address if the previous was false.
     *
     * @param orderID         - id to search for
     * @param deliveryAddress - entity to save
     * @param accountName     - account to search order for
     */
    public void addNewDeliveryAddressToOrder(@NonNull UUID orderID,
                                             @NonNull DeliveryAddress deliveryAddress,
                                             @NonNull String accountName) {
        log.trace("Adding new delivery address to existing order");
        /* Search for order entity */
        Order order = orderService.retrieveOrderFromAccount(orderID, accountName);

        /* Check if delivery order is already set, set delivery address to order and save it to database */
        if (order.getDeliveryAddress() != null) {
            log.warn("Invalid operation, Delivery Address is already assigned to this order");
            throw new InvalidOperationException(DeliveryAddressMessage.DELIVERY_ADDRESS_ALREADY_ASSIGNED);
        }
        order.setDeliveryAddress(deliveryAddress);
        orderService.updateExistingOrder(order);
    }
}
