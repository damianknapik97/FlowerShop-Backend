package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.productorder.SouvenirOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SouvenirOrderRepository extends JpaRepository<SouvenirOrder, UUID> { }
