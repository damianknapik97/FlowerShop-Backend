package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.Addon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddonRepository extends JpaRepository<Addon, UUID> {

    Iterable<Addon> findByName(String name);

    Iterable<Addon> findByNameAndColour(String name, String colour);
}
