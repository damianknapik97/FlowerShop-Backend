package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.order.OrderDTO;
import com.dknapik.flowershop.mapper.order.OrderMapper;
import com.dknapik.flowershop.mapper.order.ShoppingCartMapper;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.services.order.OrderService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@CrossOrigin
@ToString
@Log4j2
class OrderController {
    private final OrderService service;
    private final OrderMapper mapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Autowired
    OrderController(OrderService service, OrderMapper mapper, ShoppingCartMapper shoppingCartMapper) {
        this.service = service;
        this.mapper = mapper;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    /**
     * Retrieves Shopping Cart assigned to account performing this request, create order entity from it and clear it.
     *
     * @param principal - user performing this request
     * @return MessageResponseDTO containing information about operation result.
     */
    @PostMapping()
    ResponseEntity<MessageResponseDTO> createOrderFromCurrentShoppingCart(Principal principal) {
        log.traceEntry();

        UUID orderID = service.createOrderFromCurrentShoppingCart(principal.getName(), LocalDateTime.now());

        log.traceExit();
        return new ResponseEntity<>(new MessageResponseDTO(orderID.toString()), HttpStatus.CREATED);
    }

    /**
     * This end point is available only of authorized accounts.
     * Updates provided Order entity, regardless of account assigned to it.
     *
     * @param orderDTO - DTO to update (ID must match actually existing Order entity)
     * @return - MessageResponseDTO with information about operation result.
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PutMapping
    ResponseEntity<MessageResponseDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.traceEntry();

        service.updateExistingOrder(mapper.mapToEntity(orderDTO));

        log.traceExit();
        return new ResponseEntity<>(new MessageResponseDTO(OrderMessage.ORDER_UPDATED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Retrieves one page of orders, regardless of assigned account to it.
     * This end point can be used only by authorized accounts
     *
     * @param page - number of page to retrieve
     * @return Pageable with number of orders defined in ProductProperties class.
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @GetMapping("/page")
    ResponseEntity<RestResponsePage<OrderDTO>> retrieveOrdersPage(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        log.traceEntry();

        RestResponsePage<Order> orderPage = service.retrieveOrdersPage(page, ProductProperties.PAGE_SIZE);
        RestResponsePage<OrderDTO> ordeDTOPage = mapper.mapPageToDTO(orderPage);

        log.traceExit();
        return new ResponseEntity<>(ordeDTOPage, HttpStatus.OK);
    }

    /**
     * Searches currently logged user account for provided order ID and retrieves assigned to id Shopping Cart ID.
     *
     * @param orderID   - Order ID to search for in user account.
     * @param principal - User performing the request.
     * @return Shopping Cart ID assigned to Order ID.
     */
    ResponseEntity<UUID> retrieveShoppingCartID(@Valid @RequestParam("id") UUID orderID, Principal principal) {
        log.traceEntry();

        UUID shoppingCartID = service.retrieveShoppingCartID(orderID, principal.getName());

        log.traceExit();
        return new ResponseEntity<>(shoppingCartID, HttpStatus.OK);
    }
}
