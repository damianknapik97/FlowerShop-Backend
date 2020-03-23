package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.PaymentMessage;
import com.dknapik.flowershop.database.order.PaymentRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.OrderStatus;
import com.dknapik.flowershop.model.order.Payment;
import com.dknapik.flowershop.model.order.PaymentType;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;


@ToString
@Log4j2
@Service
public final class PaymentService {
    private final PaymentRepository repository;
    private final OrderService orderService;

    @Autowired
    public PaymentService(PaymentRepository repository,
                          OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;
    }

    /**
     * Search for provided Order ID inside provided account,
     * check if payment entity is already assigned to this order,
     * save the provided payment entity if the previous was false.
     *
     * @param orderID             - id to search for
     * @param payment             - entity to save
     * @param accountName         - account to search order for
     * @param expectedOrderStatus - order verification
     */
    public void addNewPaymentToOrder(@NonNull UUID orderID,
                                     @NonNull Payment payment,
                                     @NonNull String accountName,
                                     @NonNull OrderStatus expectedOrderStatus) {
        log.traceEntry("Adding new payment to existing order");
        /* Retrieve Order from provided account */
        Order order = orderService.retrieveOrderFromAccount(orderID, accountName, expectedOrderStatus);

        /* Check if payment is already assigned */
        if (order.getPayment() != null) {
            log.throwing(Level.WARN, new InvalidOperationException(PaymentMessage.PAYMENT_ALREADY_ASSIGNED));
            throw new InvalidOperationException(PaymentMessage.PAYMENT_ALREADY_ASSIGNED);
        }

        /* Set Payment for provided order and save it */
        order.setPayment(payment);
        orderService.updateExistingOrder(order);

        log.traceExit();
    }

    /**
     * Create Set collection from Enum containing all available payment options
     *
     * @return Set with Payment options.
     */
    public Set<PaymentType> retrieveAllPaymentOptions() {
        return EnumSet.allOf(PaymentType.class);
    }
}
