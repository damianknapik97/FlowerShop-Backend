package com.dknapik.flowershop.database;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, UUID> {

  //  Optional<Bouquet> findByName(String name);

  //  @Query("Select b FROM Bouquet b JOIN FETCH b.flowersSet WHERE b.id = (:id)")
   // Bouquet findByIdAndFetchFlowerPacksEagerly(@Param("id") UUID id);

    //@Query("Select b FROM Bouquet b JOIN FETCH b.flowersSet WHERE b.name = (:name)")
   // Optional<List<Bouquet>> findByNameAndFetchFlowerPacksEagerly(@Param("name") String name);
}
