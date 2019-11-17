package com.dknapik.flowershop.database;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dknapik.flowershop.model.Bouquet;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, UUID> {

    Optional<Bouquet> findByName(String name);
}
