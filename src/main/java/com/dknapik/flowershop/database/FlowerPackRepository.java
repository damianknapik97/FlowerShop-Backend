package com.dknapik.flowershop.database;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dknapik.flowershop.model.FlowerPack;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dknapik.flowershop.model.Flower;
import org.springframework.data.jpa.repository.Query;

public interface FlowerPackRepository extends JpaRepository<FlowerPack, UUID> {

    Optional<FlowerPack> findByFlower(Flower flower);

    Optional<FlowerPack> findByFlower_NameAndNumberOfFlowers(String flowerName, int numberOfFlowers);

    @Query("select f from FlowerPack f left join Bouquet b where b.id = ?1")
    List<FlowerPack> findByBouquet(UUID id);

}
