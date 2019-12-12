package com.dknapik.flowershop.database;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dknapik.flowershop.model.Flower;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Repository
public interface FlowerRepository extends JpaRepository<Flower, UUID> {
	
	Optional<Flower> findByName(String name);
}
