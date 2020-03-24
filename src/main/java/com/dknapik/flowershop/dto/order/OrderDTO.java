package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.model.order.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements DTO {
    private UUID id;
    @Size(max = 50)
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deliveryDate;
    private PaymentDTO paymentDTO;
    private DeliveryAddressDTO deliveryAddressDTO;
    @NotNull
    private ShoppingCartDTO shoppingCartDTO;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime placementDate;
    @Size(max = 1024)
    private String additionalNote;
    private OrderStatus orderStatus;
}
