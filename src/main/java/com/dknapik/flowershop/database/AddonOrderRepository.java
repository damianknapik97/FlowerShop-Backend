package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.AddonOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddonOrderRepository extends JpaRepository<AddonOrder, UUID> {
}
