package com.dknapik.flowershop.services.administration;

import com.dknapik.flowershop.constants.administration.OrderAdministrationMessage;
import com.dknapik.flowershop.constants.sorting.OrderSortingProperty;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.services.order.OrderService;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@ToString
@Log4j2
public final class OrderAdministrationService {
    private final OrderRepository repository;
    private final OrderService orderService;

    @Autowired
    public OrderAdministrationService(OrderRepository repository, OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;
    }

    /**
     * Update existing order entity by delegating to existing order update function inside Order Service.
     *
     * @param order - Entity to update
     */
    public void updateOrder(Order order) {
        orderService.updateExistingOrder(order);
    }

    /**
     * Searches database for provided order ID, and returns related to it entity.
     * After search for mentioned ID fails, ResourceNotFound runtime exception is thrown.
     */
    public Order retrieveOrder(UUID orderID) {
        log.traceEntry();
        Optional<Order> orderOptional = repository.findById(orderID);

        if (!orderOptional.isPresent()) {
            log.throwing(new ResourceNotFoundException(OrderAdministrationMessage.ORDER_NOT_FOUND));
            throw new ResourceNotFoundException(OrderAdministrationMessage.ORDER_NOT_FOUND);
        }

        log.traceExit();
        return orderOptional.get();
    }

    /**
     * Converts OrderSortingProperty enum data type into Set, and returns it.
     */
    public Set<OrderSortingProperty> retrieveSortingProperties() {
        return EnumSet.allOf(OrderSortingProperty.class);
    }

    /**
     * Creates ResponsePage containing sorted Order entities.
     *
     * @param pageNumber      - which page should be retrieved
     * @param pageSize        - how large should the page be.
     * @param sortingProperty - how orders should be ordered when retrieved from database
     * @return - RestResponsePage with Order entities.
     */
    public RestResponsePage<Order> retrieveResponsePage(int pageNumber, int pageSize,
                                                        @NonNull OrderSortingProperty sortingProperty) {
        log.traceEntry("Retrieving page of orders");

        /* Create Page request for repository and retrieve it. */
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> content = retrieveSortedPage(pageable, sortingProperty);

        /* Return collection of products ready for transport/serialization/mapping */
        log.traceExit();
        return new RestResponsePage<>(content);
    }

    /**
     * Updates Orders in provided iterable, and create new ResponsePage from provided arguments,
     * containing sorted Order entities. Newly updated orders are included.
     *
     * @param page             - which page to retrieve
     * @param numberOfEntities - how many entities to retrieve
     * @param sortingProperty  - how orders should be ordered when retrieved from database.
     * @param ordersToUpdate   - iterable containing Orders entities to update
     * @return RestResponsePage containing sorted Orders.
     */
    public RestResponsePage<Order> updateMultipleOrders(int page, int numberOfEntities,
                                                        @NonNull OrderSortingProperty sortingProperty,
                                                        @NonNull Iterable<Order> ordersToUpdate) {
        log.traceEntry();

        /* Update all entities provided in iterable */
        repository.saveAll(ordersToUpdate);
        repository.flush();

        /* Create new ResponsePage, using provided properties */
        Page<Order> newOrderPage = retrieveResponsePage(page, numberOfEntities, sortingProperty);

        log.traceExit();
        return new RestResponsePage<>(newOrderPage);
    }

    /**
     * Determines which sorting method is desired from provided enum, calls according repository function containing
     * suitable query, and returns Page object containing Order entities inside.
     * If no property matches currently defined ones in this function, default Spring sorting is used.
     *
     * @param pageable        - Page request used in repository for page retrieval
     * @param sortingProperty - Enum containing name of desired sorting method
     * @return Page with orders
     */
    private Page<Order> retrieveSortedPage(@NonNull Pageable pageable,
                                           @NonNull OrderSortingProperty sortingProperty) {
        log.traceEntry();

        Page<Order> ordersPage;
        switch (sortingProperty) {
            case PLACEMENT_DATE_DESC:
                ordersPage = repository.findAllByOrderByPlacementDateDesc(pageable);
                break;
            case PLACEMENT_DATE_ASC:
                ordersPage = repository.findAllByOrderByPlacementDateAsc(pageable);
                break;
            case DELIVERY_DATE_DESC:
                ordersPage = repository.findAllByOrderByDeliveryDateDesc(pageable);
                break;
            case DELIVERY_DATE_ASC:
                ordersPage = repository.findAllByOrderByDeliveryDateAsc(pageable);
                break;
            case ORDER_STATUS_DESC:
                ordersPage = repository.findAllByOrderByStatusDesc(pageable);
                break;
            case ORDER_STATUS_ASC:
                ordersPage = repository.findAllByOrderByStatusAsc(pageable);
                break;
            default:
                ordersPage = repository.findAll(pageable);
        }

        log.traceExit();
        return ordersPage;
    }
}
