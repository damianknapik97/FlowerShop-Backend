package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {

}
