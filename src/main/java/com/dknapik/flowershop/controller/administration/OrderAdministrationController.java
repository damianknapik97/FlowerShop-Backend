package com.dknapik.flowershop.controller.administration;

import com.dknapik.flowershop.constants.administration.OrderAdministrationMessage;
import com.dknapik.flowershop.constants.sorting.OrderSortingProperty;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.order.OrderDTO;
import com.dknapik.flowershop.mapper.order.OrderMapper;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.services.administration.OrderAdministrationService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/order-administration")
@CrossOrigin
@ToString
@Log4j2
@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
public class OrderAdministrationController {
    private final OrderAdministrationService service;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderAdministrationController(OrderAdministrationService service, OrderMapper orderMapper) {
        this.service = service;
        this.orderMapper = orderMapper;
    }

    /**
     * This end point is available only of authorized accounts.
     * Updates provided Order entity, regardless of account assigned to it.
     *
     * @param orderDTO - DTO to update (ID must match actually existing Order entity)
     * @return - MessageResponseDTO with information about operation result.
     */
    @PutMapping
    ResponseEntity<MessageResponseDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.traceEntry();

        service.updateOrder(orderMapper.mapToEntity(orderDTO));

        log.traceExit();
        return new ResponseEntity<>(
                new MessageResponseDTO(OrderAdministrationMessage.ORDER_UPDATED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Converts OrderSortingProperty enum data type into Set, and returns it.
     */
    @GetMapping("/page/sorting-properties")
    ResponseEntity<Set<OrderSortingProperty>> retrieveSortingProperties() {
        log.traceEntry();

        Set<OrderSortingProperty> sortingProperties = service.retrieveSortingProperties();

        log.traceExit();
        return new ResponseEntity<>(sortingProperties, HttpStatus.OK);
    }

    /**
     * Retrieves one page of orders, regardless of assigned account to it.
     * This end point can be used only by authorized accounts
     * <p>
     * TODO: Add Test
     *
     * @param page - number of page to retrieve
     * @return Pageable with number of orders defined in ProductProperties class.
     */
    @GetMapping("/page")
    ResponseEntity<RestResponsePage<OrderDTO>> retrieveOrdersPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "elements", defaultValue = "0") int numberOfElements,
            @RequestParam(value = "sorting", defaultValue = "NONE") OrderSortingProperty sortingProperty) {
        log.traceEntry();

        RestResponsePage<Order> orderResponsePage =
                service.retrieveResponsePage(page, numberOfElements, sortingProperty);
        RestResponsePage<OrderDTO> orderDTOResponsePage = orderMapper.mapPageToDTO(orderResponsePage);

        log.traceExit();
        return new ResponseEntity<>(orderDTOResponsePage, HttpStatus.OK);
    }

    /**
     * Update provided Iterable content, and return newly updated ResponsePage with sorting that includes
     * just updated entities.
     * <p>
     * TODO: Add Test
     */
    @PutMapping("/page")
    ResponseEntity<RestResponsePage<OrderDTO>> updateOrdersPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "elements", defaultValue = "20") int numberOfElements,
            @RequestParam(value = "sorting", defaultValue = "NONE") OrderSortingProperty sortingProperty,
            @Valid @RequestBody Iterable<Order> inputOrders) {
        log.traceEntry();

        RestResponsePage<Order> outputOrdersPage =
                service.updateMultipleOrders(page, numberOfElements, sortingProperty, inputOrders);
        RestResponsePage<OrderDTO> outputOrdersDTOPage = orderMapper.mapPageToDTO(outputOrdersPage);

        log.traceExit();
        return new ResponseEntity<>(outputOrdersDTOPage, HttpStatus.OK);
    }

    /**
     * Search database for Order ID and related to it component, and return it.
     *
     * TODO: Add Test
     */
    @GetMapping()
    ResponseEntity<OrderDTO> retrieveOrder(@Valid @RequestParam("id") UUID orderID) {
        log.traceEntry();

        Order retrievedOrder = this.service.retrieveOrder(orderID);
        OrderDTO orderDTO = orderMapper.mapToDTO(retrievedOrder);

        log.traceExit();
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
}
