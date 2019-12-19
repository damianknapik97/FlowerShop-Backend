package com.dknapik.flowershop.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShoppingCartRepository  extends JpaRepository<ShoppingCart, UUID> {

   // Optional<ShoppingCart> findFirstByOrderByCreationDateDesc();

}
