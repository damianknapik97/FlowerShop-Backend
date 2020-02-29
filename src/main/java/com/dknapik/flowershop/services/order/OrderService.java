package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.services.AccountService;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@ToString
@Log4j2
public final class OrderService {
    private final OrderRepository repository;
    private final AccountService accountService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public OrderService(OrderRepository repository,
                        AccountService accountService,
                        ShoppingCartService shoppingCartService) {
        this.repository = repository;
        this.accountService = accountService;
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * Search for provided account using account name, check if account contains provided Order ID,
     * validate it and return.
     *
     * @param orderID     - ID of order that should be contained inside provided account
     * @param accountName - login of account to search for
     * @return - Order entity matching provided ID and account
     */
    public Order retrieveOrderFromAccount(UUID orderID, String accountName) {
        log.traceEntry("Retrieving Order Entity from Account ");
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.throwing(Level.WARN, new ResourceNotFoundException(OrderMessage.NO_ORDERS));
        }

        /* Search account for provided order id */
        Order order = null;
        for (Order accountOrder : account.getOrderList()) {
            if (accountOrder.getId().equals(orderID)) {
                log.trace("Order id matches with the one inside provided account...");
                order = accountOrder;
            }
        }

        /* Check if order was found */
        if (order == null) {
            log.throwing(Level.WARN, new ResourceNotFoundException(OrderMessage.NO_ORDER_WITH_SPECIFIC_ID));
        }

        log.traceExit();
        return order;
    }

    /**
     * Retrieving account, create order from shopping cart, and save it.
     *
     * @param shoppingCart - products that shall be inside a new order
     * @param accountName  - to which account add this new order entity
     * @return - Preserved Order entity
     */
    public Order addNewOrderFromShoppingCart(@NonNull ShoppingCart shoppingCart, @NonNull String accountName) {
        log.traceEntry("Creating new order");
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.info("Account doesn't contain order list, initializing...");
            account.setOrderList(new ArrayList<>());
        }

        /* Create new Order entity and save it to provided account */
        Order order = new Order(new ShoppingCart(
                shoppingCart.getName(),
                shoppingCart.getOccasionalArticleOrderList(),
                shoppingCart.getSouvenirOrderList(),
                shoppingCart.getFlowerOrderList(),
                shoppingCart.getBouquetList()
        ));
        account.getOrderList().add(order);
        accountService.updateAccount(account);

        /* Create new shopping cart, co the old one will be detached from account */
        shoppingCartService.createNewShoppingCart(accountName);

        log.traceExit();
        return order;
    }

    /**
     * Retrieving account, searching for provided order by utilizing its ID,
     * and replacing in database existing entity with the one provided in argument.
     *
     * @param order       - entity to update
     * @param accountName - account name from which entity should be taken.
     */
    public void updateExistingOrder(@NonNull Order order, @NonNull String accountName) {
        /* Retrieve Order for provided account */
        log.traceEntry("Updating existing order");

        /* Checking if we are able to find Order entity in provided account without throwing any error */
        retrieveOrderFromAccount(order.getId(), accountName);

        /* Update entity and save it to database */
        log.debug(() -> "Saving following Order entity - " + order.toString() + " for following user " + accountName);
        repository.saveAndFlush(order);

        log.traceExit();
    }

    /**
     * Check if provided order exists in database by id, and update it
     *
     * @param order - entity to update
     */
    public void updateExistingOrder(@NonNull Order order) {
        log.traceEntry("Updating existing order");

        if (repository.existsById(order.getId())) {
            repository.saveAndFlush(order);
        } else {
            log.throwing(Level.WARN,new ResourceNotFoundException(OrderMessage.ORDER_UPDATE_FAILED));
        }

        log.traceExit();
    }

    /**
     * Retrieves page of Orders from repository.
     * By default
     *
     * @param pageNumber - which page should be retrieved
     * @param pageSize   - how large should the page be.
     * @return - RestResponsePage with Order entities.
     */
    public RestResponsePage<Order> retrieveOrdersPage(int pageNumber, int pageSize) {
        log.traceEntry("Retrieving page of orders");

        /* Create Page request for repository */
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        /* Retrieve page of orders from repository and cast it to list */
        List<Order> content = repository.findAll(pageable).getContent();

        /* Return collection of products ready for transport/serialization/mapping */
        log.traceExit();
        return new RestResponsePage<>(content, pageable, repository.count());

    }
}
