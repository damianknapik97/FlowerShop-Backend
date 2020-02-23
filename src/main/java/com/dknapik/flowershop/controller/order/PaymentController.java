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
@RequestMapping()
@CrossOrigin
@ToString
@Log4j2
public final class PaymentController {
    private final PaymentService service;
    private final PaymentMapper mapper;

    @Autowired
    public PaymentController(PaymentService service, PaymentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> createPaymentForOrder(@Valid @RequestParam("id") UUID orderID,
                                                                    @Valid PaymentDTO paymentDTO,
                                                                    Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName());

        service.addNewPaymentToOrder(orderID, mapper.mapToEntity(paymentDTO), principal.getName());

        log.trace("Building response entity");
        return new ResponseEntity<>(new MessageResponseDTO(PaymentMessage.PAYMENT_CREATED_SUCCESSFULLY),
                HttpStatus.CREATED);
    }
}
