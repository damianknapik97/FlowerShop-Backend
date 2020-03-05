package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.InternalServerException;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@ToString
@Log4j2
public final class OrderService {
    private final OrderRepository repository;
    private final AccountService accountService;
    private final ShoppingCartService shoppingCartService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    public OrderService(OrderRepository repository,
                        AccountService accountService,
                        ShoppingCartService shoppingCartService) {
        this.repository = repository;
        this.accountService = accountService;
        this.shoppingCartService = shoppingCartService;
    }


    /**
     * Retrieving account, detach shopping cart from retrieved account, save it to new Order entity,
     * add new Order entity to account, return created order ID.
     *
     * @param accountName  - to which account add this new order entity
     * @return - ID of preserved entity
     */
    public UUID createOrderFromCurrentShoppingCart(String accountName, LocalDateTime creationDate) {
        log.traceEntry();
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.warn("Account doesn't contain order list, initializing...");
            account.setOrderList(new ArrayList<>());
        }

        /* Retrieve shopping cart from account, detach it and create new shopping cart */
        ShoppingCart cart = account.getShoppingCart();

        if (shoppingCartService.isEmpty(cart)) {
            log.throwing(Level.ERROR, new InvalidOperationException(OrderMessage.NO_PRODUCTS_IN_SHOPPING_CART));
        }

        shoppingCartService.createNewShoppingCart(accountName);

        /* Create new Order entity from retrieved Shopping Cart and save it to account*/
        Order order = new Order(cart);
        order.setPlacementDate(creationDate);
        account.getOrderList().add(order);
        accountService.updateAccount(account);

        /* Retrieve Order from database to access its ID that was generated when saving to database*/
        Optional<Order> savedOrder = repository.findOrderByAccountIDAndPlacementDate(account.getId(), creationDate);
        if (!savedOrder .isPresent()) {
           log.throwing(Level.ERROR, new InternalServerException(OrderMessage.UNABLE_TO_RETRIEVE_CREATED_ORDER_ID));
        }

        log.traceExit();
        return savedOrder.get().getId();
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
