package com.dknapik.flowershop.database;

import java.util.Optional;
import java.util.UUID;

import com.dknapik.flowershop.model.FlowerPack;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dknapik.flowershop.model.Flower;

public interface FlowerPackRepository extends JpaRepository<FlowerPack, UUID> {

    Optional<FlowerPack> findByFlower(Flower flower);

}
