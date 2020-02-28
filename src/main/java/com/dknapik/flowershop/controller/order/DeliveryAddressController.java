package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.DeliveryAddressMessage;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.DeliveryAddressDTO;
import com.dknapik.flowershop.mapper.order.DeliveryAddressMapper;
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
public final class DeliveryAddressController {
    private final DeliveryAddressService service;
    private final DeliveryAddressMapper mapper;

    @Autowired
    public DeliveryAddressController(DeliveryAddressService service, DeliveryAddressMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping()
    public ResponseEntity<MessageResponseDTO> createDeliveryAddressForOrder(
            @Valid @RequestParam("id") UUID orderID,
            @Valid @RequestBody DeliveryAddressDTO deliveryAddressDTO,
            Principal principal) {
        log.info(() -> "Processing request: " + new Object() {
        }.getClass().getEnclosingMethod().getName());

        service.addNewDeliveryAddressToOrder(orderID, mapper.mapToEntity(deliveryAddressDTO), principal.getName());

        log.trace("Building response entity");
        return new ResponseEntity<>(new MessageResponseDTO(DeliveryAddressMessage.DELIVERY_ADDRESS_ADDED_SUCCESSFULLY),
                HttpStatus.CREATED);
    }
}
