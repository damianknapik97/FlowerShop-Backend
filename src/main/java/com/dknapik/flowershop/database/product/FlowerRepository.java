package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, UUID> {

    Optional<Flower> findByName(String name);

    boolean existsByNameAndHeightAndDescription(String name, int height, String description);

}
