package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.PaymentMessage;
import com.dknapik.flowershop.database.order.PaymentRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.Payment;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@ToString
@Log4j2
@Service
public final class PaymentService {
    private final PaymentRepository repository;
    private final OrderService orderService;

    @Autowired
    public PaymentService(PaymentRepository repository, OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;
    }

    /**
     * Search for provided Order ID inside provided account,
     * check if payment entity is already assigned to this order,
     * save the provided payment entity if the previous was false.
     *
     * @param orderID     - id to search for
     * @param payment     - entity to save
     * @param accountName - account to search order for
     */
    public void addNewPaymentToOrder(UUID orderID, Payment payment, String accountName) {
        log.trace("Adding new payment to existing order");
        /* Retrieve Order from provided account */
        Order order = orderService.retrieveOrderFromAccount(orderID, accountName);

        /* Check if payment is already assigned */
        if (order.getPayment() != null) {
            log.warn("Invalid operation, Payment is already assigned to this order");
            throw new InvalidOperationException(PaymentMessage.PAYMENT_ALREADY_ASSIGNED);
        }

        /* Set Payment for provided order and save it */
        order.setPayment(payment);
        orderService.updateExistingOrder(order);
    }
}
