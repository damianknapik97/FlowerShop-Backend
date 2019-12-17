package com.dknapik.flowershop.services;

import com.dknapik.flowershop.database.FlowerPackRepository;
import com.dknapik.flowershop.model.FlowerPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FlowerPackService {
    private final FlowerPackRepository flowerPackRepository;

    @Autowired
    public FlowerPackService(FlowerPackRepository flowerPackRepository) {
        this.flowerPackRepository = flowerPackRepository;
    }

    public void saveNonExistingFlowerPacks(Collection<FlowerPack> iterable) {
        for (FlowerPack fp : iterable) {
            if (!this.flowerPackRepository.findByFlower_NameAndNumberOfFlowers(
                    fp.getFlower().getName(),
                    fp.getNumberOfFlowers()).isPresent()) {
                this.flowerPackRepository.save(fp);
            }
        }
        this.flowerPackRepository.flush();
    }
}
