package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = "SELECT * FROM public.order_table ord JOIN public.account acc ON acc.id = ord.order_list_id " +
            "WHERE acc.id=?1 AND ord.placement_date=?2", nativeQuery = true)
    Optional<Order> findOrderByAccountIDAndPlacementDate(UUID accountID, LocalDateTime placementDate);

}
