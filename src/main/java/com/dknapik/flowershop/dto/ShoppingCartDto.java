package com.dknapik.flowershop.dto;

import com.dknapik.flowershop.model.Bouquet;
import com.dknapik.flowershop.model.FlowerPack;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class ShoppingCartDto {
    private UUID id;
    @NotNull
    private String name;
    private List<Bouquet> bouquetList;
    private List<FlowerPack> flowerPackList;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bouquet> getBouquetList() {
        return bouquetList;
    }

    public void setBouquetList(List<Bouquet> bouquetList) {
        this.bouquetList = bouquetList;
    }

    public List<FlowerPack> getFlowerPackList() {
        return flowerPackList;
    }

    public void setFlowerPackList(List<FlowerPack> flowerPackList) {
        this.flowerPackList = flowerPackList;
    }
}
