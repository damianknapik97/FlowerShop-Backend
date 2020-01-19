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
public class OccasionalArticleDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    private MonetaryAmount price;
    @NotBlank
    private String description;
    @NotBlank
    private String theme;
}
