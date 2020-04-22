package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByName(String name);

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM public.account acc JOIN public.order_table ord ON (acc.id = ord.order_list_id) " +
            "WHERE ord.id=?1",
            nativeQuery = true)
    Optional<Account> findByOrderID(UUID orderID);

    Page<Account> findAllByAccountByCreationDateDesc(Pageable pageable);

    Page<Account> findAllByAccountByCreationDateAsc(Pageable pageable);

    Page<Account> findAllByAccountByRoleDesc(Pageable pageable);

    Page<Account> findAllByAccountByRoleAsc(Pageable pageable);
}
