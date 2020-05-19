package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.DeliveryAddressMessage;
import com.dknapik.flowershop.database.order.DeliveryAddressRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.order.DeliveryAddress;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.OrderStatus;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import java.util.UUID;

@ToString
@Log4j2
@Service
public final class DeliveryAddressService {
    private final DeliveryAddressRepository repository;
    private final OrderService orderService;
    private final MoneyUtils moneyUtils;


    @Autowired
    public DeliveryAddressService(DeliveryAddressRepository repository,
                                  OrderService orderService,
                                  MoneyUtils moneyUtils) {
        this.repository = repository;
        this.orderService = orderService;
        this.moneyUtils = moneyUtils;
    }

    /**
     * Search for provided Order ID inside provided account,
     * check if delivery address is already assigned to this order,
     * save the provided delivery address if the previous was false.
     *
     * @param orderID             - id to search for
     * @param deliveryAddress     - entity to save
     * @param accountName         - account to search order for
     * @param expectedOrderStatus - order verification
     */
    public void addNewDeliveryAddressToOrder(@NonNull UUID orderID,
                                             @NonNull DeliveryAddress deliveryAddress,
                                             @NonNull String accountName,
                                             @NonNull OrderStatus expectedOrderStatus) {
        log.traceEntry("Adding new delivery address to existing order");

        /* Search for order entity */
        Order order = orderService.retrieveOrderFromAccount(orderID, accountName, expectedOrderStatus);

        /* Check if delivery order is already set, set delivery address to order and save it to database */
        if (order.getDeliveryAddress() != null) {
            log.throwing(Level.WARN,
                    new InvalidOperationException(DeliveryAddressMessage.ALREADY_ASSIGNED));
            throw new InvalidOperationException(DeliveryAddressMessage.ALREADY_ASSIGNED);

        }
        order.setDeliveryAddress(deliveryAddress);
        orderService.updateExistingOrder(order);

        log.traceExit();
    }

    public void updateDeliveryAddressEntity(@NonNull DeliveryAddress deliveryAddress) {
        log.traceEntry();

        if (repository.existsById(deliveryAddress.getId())) {
            repository.saveAndFlush(deliveryAddress);
        } else {
            log.throwing(new ResourceNotFoundException(DeliveryAddressMessage.UPDATE_FAILED));
            throw new ResourceNotFoundException(DeliveryAddressMessage.UPDATE_FAILED);
        }

        log.traceExit();
    }

    /**
     * TODO: This method needs to be implemented and extended, for now it returns static value.
     */
    public MonetaryAmount countDeliveryFee() {
        return moneyUtils.amountWithAppCurrency(5.00);
    }
}
