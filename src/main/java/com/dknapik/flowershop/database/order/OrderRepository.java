package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = "SELECT * FROM public.order_table ord JOIN public.account acc ON acc.id = ord.order_list_id " +
            "WHERE acc.id=?1 AND ord.placement_date=?2", nativeQuery = true)
    Optional<Order> findByAccountIDAndPlacementDate(UUID accountID, LocalDateTime placementDate);

    @Query(value = "SELECT * FROM public.order_table ord JOIN public.account acc ON acc.id = ord.order_list_id " +
            "WHERE acc.name=:name AND ord.status=:status", nativeQuery = true)
    Optional<Order> findByAccountNameAndOrderStatus(@Param("name") String accountName,
                                                    @Param("status") String orderStatus);

    @Query(value = "SELECT * FROM public.order_table ord JOIN public.account acc "
            + "ON acc.id = ord.order_list_id WHERE acc.name=?1",
            countQuery = "SELECT count(*) FROM public.order_table ord JOIN public.account acc "
                    + "ON acc.id = ord.order_list_id WHERE acc.name=?1",
            nativeQuery = true)
    Page<Order> findAllByAccountName(String accountName, Pageable pageable);

    @Query(value = "SELECT count(*) FROM public.order_table ord JOIN public.account acc "
            + "ON acc.id = ord.order_list_id WHERE acc.name=?1",
            nativeQuery = true)
    int countAllByAccountName(String accountName);
}
