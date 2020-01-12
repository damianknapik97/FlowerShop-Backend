package com.dknapik.flowershop.dto.product;

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
public class OccasionalArticleDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;
    @NotNull
    private BigDecimal price;
    @NotBlank
    private String description;
    @NotBlank
    private String theme;
}
