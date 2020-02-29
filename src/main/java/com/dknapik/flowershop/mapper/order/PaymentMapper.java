package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.PaymentDTO;
import com.dknapik.flowershop.model.order.Payment;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@ToString
@Service
public final class PaymentMapper {
    private final ModelMapper mapper;

    @Autowired
    public PaymentMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Convert Payment Entity to Payment DTO, using Jackson Model Mapper
     *
     * @param payment - entity
     * @return - dto
     */
    public PaymentDTO mapToDTO(Payment payment) {
        log.traceEntry(() -> "Mapping " + payment.getClass().getSimpleName() +
                " to " + PaymentDTO.class.getSimpleName());
        return mapper.map(payment, PaymentDTO.class);
    }

    /**
     * Convert Payment DTO to Payment Entity using Jackson Model Mapper
     *
     * @param paymentDTO - dto
     * @return - entity
     */
    public Payment mapToEntity(PaymentDTO paymentDTO) {
        log.traceEntry(() -> "Mapping " + paymentDTO.getClass().getSimpleName() +
                " to " + Payment.class.getSimpleName());
        return mapper.map(paymentDTO, Payment.class);
    }
}
