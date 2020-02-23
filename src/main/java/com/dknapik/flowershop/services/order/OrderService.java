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
        log.trace("Retrieving Order Entity from Account ");
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.warn(() -> "Couldn't update Order entity because provided account doesn't contain any orders");
            throw new ResourceNotFoundException(OrderMessage.NO_ORDERS);
        }

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
            throw new ResourceNotFoundException(OrderMessage.NO_ORDER_WITH_SPECIFIC_ID);
        }

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
        log.trace("Creating new order");
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.info("Account doesn't contain order list, initializing...");
            account.setOrderList(new ArrayList<>());
            accountService.updateAccount(account);
        }

        /* Create new Order entity and save it to provided account */
        Order order = new Order(shoppingCart);
        account.getOrderList().add(order);
        accountService.updateAccount(account);

        /* Create new shopping cart, co the old one will be detached from account */
        shoppingCartService.createNewShoppingCart(accountName);

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
        log.trace("Updating existing order");

        /* Checking if we are able to find Order entity in provided account without throwing any error */
        retrieveOrderFromAccount(order.getId(), accountName);

        /* Update entity and save it to database */
        log.debug(() -> "Saving following Order entity - " + order.toString() + " for following user " + accountName);
        repository.saveAndFlush(order);
    }

    /**
     * Check if provided order exists in database by id, and update it
     *
     * @param order - entity to update
     */
    public void updateExistingOrder(@NonNull Order order) {
        log.trace("Updating existing order");
        if (repository.existsById(order.getId())) {
            repository.saveAndFlush(order);
        } else {
            log.warn("Couldn't update order because it couldn't be found in database");
            throw new ResourceNotFoundException(OrderMessage.ORDER_UPDATE_FAILED);
        }
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
        log.trace("Retrieving page of orders");

        /* Create Page request for repository */
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        /* Retrieve page of orders from repository and cast it to list */
        List<Order> content = repository.findAll(pageable).getContent();

        /* Return collection of products ready for transport/serialization/mapping */
        return new RestResponsePage<>(content, pageable, repository.count());

    }
}
