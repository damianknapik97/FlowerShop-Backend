package com.dknapik.flowershop.database;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dknapik.flowershop.model.Flower;


@Repository
public interface FlowerRepository extends JpaRepository<Flower, UUID> {
	Flower findByName(String name);

}
