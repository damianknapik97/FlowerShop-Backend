package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.order.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {

}
