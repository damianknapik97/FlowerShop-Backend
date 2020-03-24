package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.DeliveryAddressMessage;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.DeliveryAddressDTO;
import com.dknapik.flowershop.mapper.order.DeliveryAddressMapper;
import com.dknapik.flowershop.model.order.OrderStatus;
import com.dknapik.flowershop.services.order.DeliveryAddressService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/delivery-address")
@CrossOrigin
@ToString
@Log4j2
final class DeliveryAddressController {
    private final DeliveryAddressService service;
    private final DeliveryAddressMapper mapper;

    @Autowired
    DeliveryAddressController(DeliveryAddressService service, DeliveryAddressMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Search account performing this request for provided Order ID, create new DeliveryAddress entity
     * and assign it to mentioned Order entity.
     *
     * @param orderID            - Order to search for
     * @param deliveryAddressDTO - Delivery Address DTO to cast to entity and save it to found order.
     * @param principal          - account performing this request and having assigned Order to it.
     * @return MessageResponseDTO containing information about operation result.
     */
    @PostMapping()
    ResponseEntity<MessageResponseDTO> createDeliveryAddressForOrder(
            @Valid @RequestParam("id") UUID orderID,
            @Valid @RequestBody DeliveryAddressDTO deliveryAddressDTO,
            Principal principal) {
        log.traceEntry();
        OrderStatus expectedStatus = OrderStatus.CREATED;

        service.addNewDeliveryAddressToOrder(orderID, mapper.mapToEntity(deliveryAddressDTO),
                principal.getName(), expectedStatus);

        log.traceExit();
        return new ResponseEntity<>(new MessageResponseDTO(DeliveryAddressMessage.DELIVERY_ADDRESS_ADDED_SUCCESSFULLY),
                HttpStatus.CREATED);
    }
}
