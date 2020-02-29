package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.order.OrderDTO;
import com.dknapik.flowershop.dto.order.ShoppingCartDTO;
import com.dknapik.flowershop.mapper.order.OrderMapper;
import com.dknapik.flowershop.mapper.order.ShoppingCartMapper;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.services.order.OrderService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/order")
@CrossOrigin
@ToString
@Log4j2
final class OrderController {
    private final OrderService service;
    private final OrderMapper mapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Autowired
    OrderController(OrderService service, OrderMapper mapper, ShoppingCartMapper shoppingCartMapper) {
        this.service = service;
        this.mapper = mapper;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    @PostMapping()
    ResponseEntity<OrderDTO> createOrderFromShoppingCart(@Valid @RequestBody ShoppingCartDTO shoppingCartDTO,
                                                         Principal principal) {
        log.traceEntry();

        /* Create new Order, Save it to account, Cast it To DTO */
        Order order = service.addNewOrderFromShoppingCart(shoppingCartMapper.mapToEntity(shoppingCartDTO),
                principal.getName());
        OrderDTO orderDTO = mapper.mapToDTO(order);

        log.traceExit();
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    /* TODO Add Permissions as this action will be only allowed for authorized users */
    @PutMapping
    ResponseEntity<MessageResponseDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO, Principal principal) {
        log.traceEntry();

        service.updateExistingOrder(mapper.mapToEntity(orderDTO));

        log.traceExit();
        return new ResponseEntity<>(new MessageResponseDTO(OrderMessage.ORDER_UPDATED_SUCCESSFULLY), HttpStatus.OK);
    }

    /* TODO Add Permissions as this action will be only allowed for authorized users */
    @GetMapping("/page")
    ResponseEntity<RestResponsePage<OrderDTO>> retrieveOrdersPage(@RequestParam("page") int page) {
        log.traceEntry();

        RestResponsePage<Order> orderPage = service.retrieveOrdersPage(page, ProductProperties.PAGE_SIZE);
        RestResponsePage<OrderDTO> ordeDTOPage = mapper.mapPageToDTO(orderPage);

        log.traceExit();
        return new ResponseEntity<>(ordeDTOPage, HttpStatus.OK);
    }
}
