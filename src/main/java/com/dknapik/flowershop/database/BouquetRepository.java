package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, UUID> {

   // List<Bouquet> findAllByUserCreated(boolean userCreated, Pageable pageable);

}
