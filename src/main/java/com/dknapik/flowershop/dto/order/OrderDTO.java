package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UUID id;
    @NotBlank
    private String message;
    private LocalDateTime deliveryDate;
    @NotNull
    private PaymentDTO paymentDTO;
    @NotNull
    private DeliveryAddressDTO deliveryAddressDTO;
    @NotNull
    private ShoppingCart shoppingCart;
    private LocalDateTime placementDate;
}
