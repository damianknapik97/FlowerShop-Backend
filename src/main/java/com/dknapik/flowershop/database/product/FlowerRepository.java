package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.Flower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FlowerRepository extends JpaRepository<Flower, UUID> {

    Optional<Flower> findByName(String name);

}
