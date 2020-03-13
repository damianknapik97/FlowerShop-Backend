package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.PaymentMessage;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.PaymentDTO;
import com.dknapik.flowershop.mapper.order.PaymentMapper;
import com.dknapik.flowershop.model.order.PaymentType;
import com.dknapik.flowershop.services.order.PaymentService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
@CrossOrigin
@ToString
@Log4j2
final class PaymentController {
    private final PaymentService service;
    private final PaymentMapper mapper;

    @Autowired
    PaymentController(PaymentService service, PaymentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Create new Payment entity and assign it to provided Order ID that is assigned to currently logged user.
     *
     * @param orderID    - Order to assign Payment entity to
     * @param paymentDTO - Payment entity to assign.
     * @param principal  - User performing the request to check if Order is assigned to him.
     * @return MessageResponseDTO with operation result status,
     */
    @PostMapping
    ResponseEntity<MessageResponseDTO> createPaymentForOrder(@Valid @RequestParam("id") UUID orderID,
                                                             @Valid @RequestBody PaymentDTO paymentDTO,
                                                             Principal principal) {
        log.traceEntry();

        service.addNewPaymentToOrder(orderID, mapper.mapToEntity(paymentDTO), principal.getName());

        log.traceExit();
        return new ResponseEntity<>(new MessageResponseDTO(PaymentMessage.PAYMENT_CREATED_SUCCESSFULLY),
                HttpStatus.CREATED);
    }


    /**
     * // TODO: Add Test
     * <p>
     * Retrieve all possible values of PaymentType that can be set to Payment entity.
     *
     * @return EnumSet created from PaymentType enum.
     */
    @GetMapping("/types")
    ResponseEntity<Set<PaymentType>> getPaymentTypes() {
        log.traceEntry();

        Set<PaymentType> paymentTypes = service.retrieveAllPaymentOptions();

        log.traceExit();
        return new ResponseEntity<>(paymentTypes, HttpStatus.OK);
    }
}
