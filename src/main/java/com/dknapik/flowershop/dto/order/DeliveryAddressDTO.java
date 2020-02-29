package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDTO implements DTO {
    private UUID id;
    @NotBlank
    private String cityName;
    @NotBlank
    private String zipCode;
    @NotBlank
    private String streetName;
    @NotBlank
    private String houseNumber;
    private String apartmentNumber;
}
