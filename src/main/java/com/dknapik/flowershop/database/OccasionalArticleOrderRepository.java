package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.OccasionalArticleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OccasionalArticleOrderRepository extends JpaRepository<OccasionalArticleOrder,UUID> {

}
