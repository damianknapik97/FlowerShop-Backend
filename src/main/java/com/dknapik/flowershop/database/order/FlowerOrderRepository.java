package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.productorder.FlowerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlowerOrderRepository extends JpaRepository<FlowerOrder, UUID> {
}
