package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.DeliveryAddressMessage;
import com.dknapik.flowershop.database.order.DeliveryAddressRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.DeliveryAddress;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.services.AccountService;
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
    private final AccountService accountService;
    private final OrderService orderService;


    @Autowired
    public DeliveryAddressService(DeliveryAddressRepository repository,
                                  AccountService accountService,
                                  OrderService orderService) {
        this.repository = repository;
        this.accountService = accountService;
        this.orderService = orderService;
    }

    public void addNewDeliveryAddressToOrder(@NonNull UUID orderID,
                                             @NonNull DeliveryAddress deliveryAddress,
                                             @NonNull String accountName) {
        Account account = accountService.retrieveAccountByName(accountName);

        /* Search account for provided order id */
        Order order = null;
        for (Order accountOrder : account.getOrderList()) {
            if (accountOrder.getId().equals(orderID)) {
                order = accountOrder;
            }
        }

        /* Check if order was found */
        if (order == null) {
            log.warn("Couldn't find order with provided ID, assigned to provided user");
            throw new ResourceNotFoundException(DeliveryAddressMessage.ORDER_NOT_FOUND);
        }

        /* Check if delivery order is already set, set delivery address to order and save it to database */
        if (order.getDeliveryAddress() != null) {
            log.warn("Invalid operation, Delivery Address is already assigned to this order");
            throw new InvalidOperationException(DeliveryAddressMessage.DELIVERY_ADDRESS_ALREADY_ASSIGNED);
        }
        order.setDeliveryAddress(deliveryAddress);
        orderService.updateExistingOrder(order);
    }
}
