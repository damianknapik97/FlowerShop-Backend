package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OccasionalArticleOrderRepository extends JpaRepository<OccasionalArticleOrder,UUID> {

}
