package com.dknapik.flowershop.dto.product;

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
public class FlowerDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    private MonetaryAmount price;
    @NotNull
    private String description;
    private int height;
}
