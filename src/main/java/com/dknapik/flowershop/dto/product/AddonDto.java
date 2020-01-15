package com.dknapik.flowershop.dto.product;

import com.dknapik.flowershop.model.product.AddonColour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddonDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    private AddonColour colour;
    @NotNull
    private BigDecimal amount; // Money Amount - name like that is required by MoneyModule in ObjectMapper
    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;  // Currency Unit - name name like that is required by MoneyModule in ObjectMapper
    @NotNull
    private String description;
}