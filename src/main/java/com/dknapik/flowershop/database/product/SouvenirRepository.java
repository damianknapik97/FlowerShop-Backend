package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.Souvenir;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SouvenirRepository extends JpaRepository<Souvenir, UUID> {

    Optional<Souvenir> findByName(String name);

}
