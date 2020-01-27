package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.model.order.FlowerOrder;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.SouvenirOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<OccasionalArticleOrder> occasionalArticleOrderList;
    private List<SouvenirOrder> souvenirOrderList;
    private List<FlowerOrder> flowerOrderList;
    private List<Bouquet> bouquetList;
}
