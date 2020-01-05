package com.dknapik.flowershop.model.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String cityName;
    @Column(nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String streetName;
    @Column(nullable = false)
    private String houseNumber;
    @Column
    private String apartmentNumber;


    public DeliveryAddress(String cityName,
                           String zipCode,
                           String streetName,
                           String houseNumber) {
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }

    public DeliveryAddress(String cityName,
                           String zipCode,
                           String streetName,
                           String houseNumber,
                           String apartmentNumber) {
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }
}
