package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.Order;
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

@Service
@ToString
@Log4j2
public final class OrderService {
    private final OrderRepository repository;
    private final AccountService accountService;

    @Autowired
    public OrderService(OrderRepository repository, AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }

    /**
     * Retrieving account, and adding provided Order to it.
     *
     * @param order
     * @param accountName
     */
    public void addNewOrder(@NonNull Order order, @NonNull String accountName) {
        log.trace("Adding new order");
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.info("Account doesn't contain order list, initializing...");
            account.setOrderList(new ArrayList<>());
            accountService.updateAccount(account);
        }

        /* Add provided Order entity to Account and save it */
        log.debug(() -> "Saving following Order entity - " + order.toString() + " for following user " + accountName);
        account.getOrderList().add(order);
        accountService.updateAccount(account);
    }

    /**
     * Retrieving account, searching for provided order by utilizing its ID,
     * and replacing in database existing entity with the one provided in argument.
     *
     * @param order
     * @param accountName
     */
    public void updateExistingOrder(@NonNull Order order, @NonNull String accountName) {
        log.trace("Updating existing order");
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.warn(() -> "Couldn't update Order entity because provided account doesn't contain any orders");
            throw new ResourceNotFoundException(OrderMessage.NO_ORDERS);
        }

        /* Search for entity that would match id of provided entity in provided account */
        Order entityToUpdate = null;
        for (Order listContent : account.getOrderList()) {
            if (listContent.getId().equals(order.getId())) {
                entityToUpdate = listContent;
                break;
            }
        }

        /* Check if entity was found */
        if (entityToUpdate == null) {
            log.warn(() -> "No order entity inside provided account matches the provided entity id: " + order.getId());
            throw new ResourceNotFoundException(OrderMessage.NO_ORDER_WITH_SPECIFIC_ID);
        }

        log.debug(() -> "Saving following Order entity - " + order.toString() + " for following user " + accountName);
        entityToUpdate = order;
        repository.saveAndFlush(entityToUpdate);
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
