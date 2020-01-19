package com.dknapik.flowershop.dto.product;

import com.dknapik.flowershop.model.product.AddonColour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private MonetaryAmount price;
    @NotNull
    private String description;
}
