package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.AddonOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddonOrderRepository extends JpaRepository<AddonOrder, UUID> {
}
