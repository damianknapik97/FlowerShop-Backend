package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.bouquet.BouquetDTO;
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
public class ShoppingCartDTO {
    @NotNull
    private UUID id;
    private String name;
    private @Valid List<OccasionalArticleOrderDTO> occasionalArticleOrderDTOs;
    private @Valid List<SouvenirOrderDTO> souvenirOrderDTOs;
    private @Valid List<FlowerOrderDTO> flowerOrderDTOs;
    private @Valid List<BouquetDTO> bouquetDTOS;
}
