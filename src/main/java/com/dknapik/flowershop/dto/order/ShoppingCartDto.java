package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.bouquet.BouquetDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {
    @NotNull
    private UUID id;
    private String name;
    private @Valid List<OccasionalArticleOrderDto> occasionalArticleOrdersDtos;
    private @Valid List<SouvenirOrderDto> souvenirOrderDtos;
    private @Valid List<FlowerOrderDto> flowerOrderDtos;
    private @Valid List<BouquetDto> bouquetsDtos;
}
