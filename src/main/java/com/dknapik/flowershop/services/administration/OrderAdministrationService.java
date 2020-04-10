package com.dknapik.flowershop.services.administration;

import com.dknapik.flowershop.constants.sorting.OrderSortingProperty;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
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
import java.util.List;
import java.util.Set;

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
     * Updates content of provided ResponsePage, uses it to build a new ResponsePage containing newly ordered
     * components, including the changes.
     *
     * @param pageToUpdate    - Page containing orders to update, and to create new query.
     * @param sortingProperty - how orders should be ordered when retrieved from database.
     * @return RestResponsePage containing sorted Orders.
     */
    public RestResponsePage<Order> updatePage(@NonNull RestResponsePage<Order> pageToUpdate,
                                              @NonNull OrderSortingProperty sortingProperty) {
        log.traceEntry();

        /* Update all entities provided in page content */
        List<Order> pageContent = pageToUpdate.getContent();
        for (Order order : pageContent) {
            orderService.updateExistingOrder(order);
        }

        /* Create new ResponsePage, using provided pageable and sorting property */
        Page<Order> newOrderPage = retrieveSortedPage(pageToUpdate.getPageable(), sortingProperty);

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
