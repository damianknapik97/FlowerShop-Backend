package com.dknapik.flowershop.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
    @NotBlank
    private String message;
    @NotBlank
    private LocalDateTime deliveryDate;
    @NotBlank
    private String additionalNote;
}
