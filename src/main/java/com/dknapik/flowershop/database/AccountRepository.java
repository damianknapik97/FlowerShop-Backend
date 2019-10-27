package com.dknapik.flowershop.database;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dknapik.flowershop.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
	
	Optional<Account> findByName(String name);
	
	boolean existsByName(String name);

}
