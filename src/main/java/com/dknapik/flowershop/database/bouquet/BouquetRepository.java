package com.dknapik.flowershop.database.bouquet;

import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.model.bouquet.BouquetAddon;
import com.dknapik.flowershop.model.bouquet.BouquetFlower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, UUID> {

    boolean existsByNameAndDiscountPercentageAndUserCreated(String name, int discountPercentage, boolean userCreated);

}
