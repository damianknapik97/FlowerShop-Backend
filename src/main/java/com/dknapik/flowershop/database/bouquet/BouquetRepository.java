package com.dknapik.flowershop.database.bouquet;

import com.dknapik.flowershop.model.bouquet.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, UUID> {

   // List<Bouquet> findAllByUserCreated(boolean userCreated, Pageable pageable);

}
