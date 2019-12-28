package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.FlowerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlowerOrderRepository extends JpaRepository<FlowerOrder, UUID> {
}
