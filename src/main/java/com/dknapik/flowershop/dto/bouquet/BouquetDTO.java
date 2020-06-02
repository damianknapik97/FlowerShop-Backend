package com.dknapik.flowershop.dto.bouquet;

import com.dknapik.flowershop.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BouquetDTO implements DTO {
    private UUID id;
    private String name;
    private List<BouquetFlowerDTO> bouquetFlowerList;
    private List<BouquetAddonDTO> bouquetAddonList;
    private String imageLarge;
    private String imageMedium;
    private String imageSmall;
    private MonetaryAmount totalPrice;
    private boolean userCreated;
}
