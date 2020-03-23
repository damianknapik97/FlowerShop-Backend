package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.InternalServerException;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.exceptions.runtime.UnauthorizedException;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.OrderStatus;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.services.AccountService;
import com.dknapik.flowershop.utils.ValidationUtility;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@ToString
@Log4j2
public final class OrderService {
    private final OrderRepository repository;
    private final AccountService accountService;
    private final ShoppingCartService shoppingCartService;
    private final ValidationUtility validationUtility;

    @Autowired
    public OrderService(OrderRepository repository,
                        AccountService accountService,
                        ShoppingCartService shoppingCartService,
                        ValidationUtility validationUtility) {
        this.repository = repository;
        this.accountService = accountService;
        this.shoppingCartService = shoppingCartService;
        this.validationUtility = validationUtility;
    }

    /**
     * Retrieving account, detach shopping cart from retrieved account, save it to new Order entity,
     * add new Order entity to account, return created order ID.
     *
     * @param accountName - to which account add this new order entity
     * @return - ID of preserved entity
     */
    public UUID createOrderFromCurrentShoppingCart(@NonNull String accountName, @NonNull LocalDateTime creationDate) {
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
            log.throwing(Level.WARN, new InvalidOperationException(OrderMessage.NO_PRODUCTS_IN_SHOPPING_CART));
            throw new InvalidOperationException(OrderMessage.NO_PRODUCTS_IN_SHOPPING_CART);
        }

        shoppingCartService.createNewShoppingCart(accountName);

        /* Create new Order entity from retrieved Shopping Cart and save it to account*/
        Order order = new Order(cart);
        order.setPlacementDate(creationDate);
        account.getOrderList().add(order);
        accountService.updateAccount(account);

        /* Retrieve Order from database to access its ID that was generated when saving to database*/
        Optional<Order> savedOrder = repository.findByAccountIDAndPlacementDate(account.getId(), creationDate);
        if (!savedOrder.isPresent()) {
            log.throwing(Level.ERROR, new InternalServerException(OrderMessage.UNABLE_TO_RETRIEVE_CREATED_ORDER_ID));
            throw new InternalServerException(OrderMessage.UNABLE_TO_RETRIEVE_CREATED_ORDER_ID);
        }

        log.traceExit();
        return savedOrder.get().getId();
    }

    /**
     * Search for provided account using account name, check if account contains provided Order ID,
     * validate order status and return.
     *
     * @param orderID        - ID of order that should be contained inside provided account
     * @param accountName    - login of account to search for
     * @param expectedStatus - param to validate and ensure if request is being made to expected Order entity.
     * @return - Order entity matching provided ID and account
     */
    public Order retrieveOrderFromAccount(@NonNull UUID orderID,
                                          @NonNull String accountName,
                                          @NonNull OrderStatus expectedStatus) {
        log.traceEntry("Retrieving Order Entity from Account ");
        Account account = accountService.retrieveAccountByName(accountName);

        /* Check if Account even have initialized list */
        if (account.getOrderList() == null) {
            log.throwing(Level.WARN, new ResourceNotFoundException(OrderMessage.NO_ORDERS));
            throw new ResourceNotFoundException(OrderMessage.NO_ORDERS);
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
            throw new ResourceNotFoundException(OrderMessage.NO_ORDER_WITH_SPECIFIC_ID);
        }

        /* Check if order status matches expectations */
        if (!validateOrderStatus(order, expectedStatus)) {
            log.throwing(Level.ERROR,
                    new UnauthorizedException("Requested order status doesn't match request expectations"));
            throw new UnauthorizedException("Requested order status doesn't match request expectations");
        }

        log.traceExit();
        return order;
    }

    /**
     * Searches for order with provided id in provided account and returns its shopping cart ID.
     *
     * @param orderID        - order to retrieve shopping cart from
     * @param accountName    - account to serach for order id in.
     * @param expectedStatus - order verification
     * @return UUID containg shopping cart ID assigned to provided Order ID.
     */
    public UUID retrieveShoppingCartID(@NonNull UUID orderID, String accountName, @NonNull OrderStatus expectedStatus) {
        log.traceEntry();
        Order retrievedOrder = retrieveOrderFromAccount(orderID, accountName, expectedStatus);

        /* Check if Order contains shopping cart */
        if (retrievedOrder.getShoppingCart() == null) {
            log.throwing(Level.ERROR, new InvalidOperationException(OrderMessage.NO_SHOPPING_CART_ASSIGNED));
            throw new InvalidOperationException(OrderMessage.NO_SHOPPING_CART_ASSIGNED);
        }

        log.traceExit();
        return retrievedOrder.getShoppingCart().getId();
    }

    /**
     * Updates retrieved order from provided account by provided arguments and saves it to database.
     *
     * @param orderID        - Order to search for
     * @param accountName    - account in which to search for provided Order ID
     * @param message        - String containing message that should be delivered on a note
     * @param deliveryDate   - Date at which Order should be delivered.
     * @param additionalNote - Additional notes about this order.
     * @param expectedStatus - order verification
     */
    public void updateOrderDetails(@NonNull UUID orderID, String accountName, String message,
                                   LocalDateTime deliveryDate, String additionalNote,
                                   @NonNull OrderStatus expectedStatus) {
        log.traceEntry();
        Order retrievedOrder = retrieveOrderFromAccount(orderID, accountName, expectedStatus);

        /* Set provided argument to retrieved shopping cart and save it */
        retrievedOrder.setMessage(message);
        retrievedOrder.setDeliveryDate(deliveryDate);
        retrievedOrder.setAdditionalNote(additionalNote);
        repository.saveAndFlush(retrievedOrder);

        log.traceExit();
    }

    public void validateOrderAndChangeItsStatus(@NonNull UUID orderID,
                                                @NonNull String accountName,
                                                @NonNull OrderStatus expectedStatus) {
        log.traceEntry();
        Order order = retrieveOrderFromAccount(orderID, accountName, expectedStatus);

        /* Run all validation on provided order */
        boolean validationResult = validationUtility.validateOrderEntity(order)
                && validationUtility.validateShoppingCartEntity(order.getShoppingCart())
                && validationUtility.validateDeliveryAddressEntity(order.getDeliveryAddress())
                && validationUtility.validatePaymentEntity(order.getPayment());

        /* Check if validation passed */
        if (!validationResult) {
            log.throwing(Level.ERROR, new InvalidOperationException(OrderMessage.VALIDATION_FAILURE));
            throw new InvalidOperationException(OrderMessage.VALIDATION_FAILURE);
        }

        /* Change status of Order entity to let employees know that it is available for processing  */
        order.setStatus(OrderStatus.WAITING_FOR_ASSIGNMENT);
        updateExistingOrder(order);

        log.traceExit();
    }

    /**
     * Searches provided account for unfinished Order entity containing Created status.
     * Order entity is considered unfinished when its owner interrupted Order creation process
     * by either leaving or refreshing website.
     * If no Order with Created status is found, NULL is returned.
     *
     * @param accountName - Current user account
     * @return NULL if no Order entity with Created status was found. Order entity otherwise.
     */
    @Nullable
    public Order retrieveUnplacedOrder(String accountName) {
        log.traceEntry();

        Order unfinishedOrder = repository.findByAccountNameAndOrderStatus(accountName, OrderStatus.CREATED.toString())
                .orElse(null);

        log.traceExit();
        return unfinishedOrder;
    }

    /**
     * Removes provided Order ID from provided account. For this operation to be successfully performed,
     * Order entity must match provided status
     *
     * @param orderID        - Order to search for
     * @param accountName    - account to search in
     * @param expectedStatus - expected status of removed order
     */
    public void removeOrder(UUID orderID, String accountName, OrderStatus expectedStatus) {
        log.traceEntry();

        Order order = retrieveOrderFromAccount(orderID, accountName, expectedStatus);
        repository.delete(order);
        repository.flush();

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
            log.throwing(Level.WARN, new ResourceNotFoundException(OrderMessage.ORDER_UPDATE_FAILED));
            throw new ResourceNotFoundException(OrderMessage.ORDER_UPDATE_FAILED);
        }

        log.traceExit();
    }

    /**
     * Retrieves a page of Order entities assigned to provided account name
     *
     * @param pageNumber -
     * @param pageSize -
     * @param accountName - Account to retrieved Orders page from
     * @return RestResponsePage containing Order entities depending on provided arguments.
     */
    public RestResponsePage<Order> retrieveOrdersPageFromAccount(int pageNumber, int pageSize, String accountName) {

        /* Create Page request for repository */
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);

        /* Retrieve List of Orders and return RestResponsePage created from this List. */
        List<Order> orderList = repository.findAllByAccountName(accountName, pageRequest).getContent();
        return new RestResponsePage<>(orderList, pageRequest, repository.countAllByAccountName(accountName));
    }

    /**
     * Check if provided Order matches the expected status
     *
     * @param order          - entity to check.
     * @param expectedStatus - status to compare to.
     * @return true if order status matches the expected status.
     */
    private boolean validateOrderStatus(@NonNull Order order, @NonNull OrderStatus expectedStatus) {
        return order.getStatus() == expectedStatus;
    }
}
