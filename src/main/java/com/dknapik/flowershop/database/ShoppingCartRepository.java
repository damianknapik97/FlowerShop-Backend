package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository  extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findByAccount(Account account);
}
