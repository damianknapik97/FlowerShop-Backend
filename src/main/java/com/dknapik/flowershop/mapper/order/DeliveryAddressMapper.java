package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.DeliveryAddressDTO;
import com.dknapik.flowershop.model.order.DeliveryAddress;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@ToString
@Service
public final class DeliveryAddressMapper {
    private final ModelMapper mapper;

    @Autowired
    public DeliveryAddressMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Map provided DeliveryAddress entity to DeliveryAddress DTO using Jackson Model Mapper.
     *
     * @param deliveryAddress - entity
     * @return - dto
     */
    public DeliveryAddressDTO mapToDTO(DeliveryAddress deliveryAddress) {
        log.traceEntry("Mapping " + deliveryAddress.getClass().getSimpleName() + " to "
                + DeliveryAddressDTO.class.getSimpleName());
        return mapper.map(deliveryAddress, DeliveryAddressDTO.class);
    }

    /**
     * Map provided DeliveryAddress DTO to DeliveryAddress Entity using Jackson Model Mapper.
     *
     * @param deliveryAddressDTO - dto
     * @return - entity
     */
    public DeliveryAddress mapToEntity(DeliveryAddressDTO deliveryAddressDTO) {
        log.traceEntry(() -> "Mapping " + deliveryAddressDTO.getClass().getSimpleName() +
                " to " + DeliveryAddress.class.getSimpleName());
        return mapper.map(deliveryAddressDTO, DeliveryAddress.class);
    }
}
