package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository  extends JpaRepository<ShoppingCart, UUID> {

    List<ShoppingCart> findByAccount_ID(UUID accountID);

    Optional<ShoppingCart> findFirstOrderByCreationDateDesc();

}
