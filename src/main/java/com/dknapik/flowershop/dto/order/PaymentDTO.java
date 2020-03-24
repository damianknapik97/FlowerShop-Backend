package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.model.order.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements DTO {
    private UUID id;
    @NotNull
    private MonetaryAmount totalPrice;
    @NotNull
    private PaymentType paymentType;
    private boolean wasPaid;
}
