package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.PaymentMessage;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.PaymentDTO;
import com.dknapik.flowershop.mapper.order.PaymentMapper;
import com.dknapik.flowershop.services.order.PaymentService;
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
}
