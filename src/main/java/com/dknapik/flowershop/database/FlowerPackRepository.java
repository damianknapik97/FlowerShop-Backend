package com.dknapik.flowershop.database;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dknapik.flowershop.model.Flower;

public interface FlowerPackRepository extends JpaRepository<Flower, UUID> {

}
