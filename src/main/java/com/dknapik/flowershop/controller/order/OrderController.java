package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.order.OrderDTO;
import com.dknapik.flowershop.mapper.order.OrderMapper;
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
public final class OrderController {
    public final OrderService service;
    public final OrderMapper mapper;

    @Autowired
    public OrderController(OrderService service, OrderMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> placeOrder(@Valid @RequestBody OrderDTO orderDTO, Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName());

        service.addNewOrder(mapper.mapToEntity(orderDTO), principal.getName());

        log.trace("Building response entity");
        return new ResponseEntity<>(new MessageResponseDTO(OrderMessage.ORDER_PLACED_SUCCESSFULLY), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<MessageResponseDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO, Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName());

        service.updateExistingOrder(mapper.mapToEntity(orderDTO), principal.getName());

        log.trace("Building response entity");
        return new ResponseEntity<>(new MessageResponseDTO(OrderMessage.ORDER_UPDATED_SUCCESSFULLY), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<RestResponsePage<OrderDTO>> retrieveOrdersPage(@RequestParam("page") int page) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName());

        RestResponsePage<Order> orderPage = service.retrieveOrdersPage(page, ProductProperties.PAGE_SIZE);
        RestResponsePage<OrderDTO> ordeDTOPage = mapper.mapPageToDTO(orderPage);

        log.trace("Building response entity");
        return new ResponseEntity<>(ordeDTOPage, HttpStatus.OK);
    }
}
