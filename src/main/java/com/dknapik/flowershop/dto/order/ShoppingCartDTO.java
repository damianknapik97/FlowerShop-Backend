package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.dto.bouquet.BouquetDTO;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO implements DTO {
    @NotNull
    private UUID id;
    private String name;
    private @Valid List<OccasionalArticleOrderDTO> occasionalArticleOrderDTOs;
    private @Valid List<SouvenirOrderDTO> souvenirOrderDTOs;
    private @Valid List<FlowerOrderDTO> flowerOrderDTOs;
    private @Valid List<BouquetDTO> bouquetDTOs;
    private LocalDateTime creationDate;

    /**
     *
     * @param allProductOrders
     */
    public void setProductOrderDTOs(List<List<? extends ProductOrderDTO>> allProductOrders) {
        for (List<? extends ProductOrderDTO> productOrder : allProductOrders) {
            if (productOrder != null && !productOrder.isEmpty()) {
                if (productOrder.get(0).compareClass(FlowerOrderDTO.class)) {
                    flowerOrderDTOs = Arrays.asList(productOrder.toArray(new FlowerOrderDTO[productOrder.size()]));
                } else if (productOrder.get(0).compareClass(SouvenirOrderDTO.class)) {
                    souvenirOrderDTOs = Arrays.asList(productOrder.toArray(new SouvenirOrderDTO[productOrder.size()]));
                } else if (productOrder.get(0).compareClass(OccasionalArticleOrder.class)) {
                    occasionalArticleOrderDTOs = Arrays.asList(productOrder.toArray(
                            new OccasionalArticleOrderDTO[productOrder.size()]));
                }
            }
        }
    }
}
