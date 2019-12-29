package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.SouvenirOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SouvenirOrderRepository extends JpaRepository<SouvenirOrder, UUID> { }