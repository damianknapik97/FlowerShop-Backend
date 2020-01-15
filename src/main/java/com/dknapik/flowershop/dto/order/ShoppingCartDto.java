package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.model.order.FlowerOrder;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.SouvenirOrder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class ShoppingCartDto {
    @NotNull
    private UUID id;
    private String name;
    List<OccasionalArticleOrder> occasionalArticleOrderList;
    List<SouvenirOrder> souvenirOrderList;
    List<FlowerOrder> flowerOrderList;
    List<Bouquet> bouquetList;
}
