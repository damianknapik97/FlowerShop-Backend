package com.dknapik.flowershop.database.bouquet;

import com.dknapik.flowershop.model.bouquet.Bouquet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, UUID> {

    boolean existsByNameAndDiscountPercentageAndUserCreated(String name, int discountPercentage, boolean userCreated);

    Page<Bouquet> findAllByOrderByNameDesc(Pageable pageable);

    Page<Bouquet> findAllByOrderByNameAsc(Pageable pageable);

    Page<Bouquet> findAllByOrderByProductionCostDesc(Pageable pageable);

    Page<Bouquet> findAllByOrderByProductionCostAsc(Pageable pageable);
}
