package com.dknapik.flowershop.database.bouquet;

import com.dknapik.flowershop.model.bouquet.BouquetAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BouquetAddonRepository extends JpaRepository<BouquetAddon, UUID> {
}
